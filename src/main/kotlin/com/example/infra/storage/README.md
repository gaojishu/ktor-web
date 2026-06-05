# Storage 对象存储模块

统一的对象存储抽象层，屏蔽不同云厂商 OSS 的实现差异。业务代码通过 `StorageFactory` 获取 `StorageProvider`，无需关心底层是阿里云还是其他服务商。

## 架构概览

```
业务层 (Service / Controller)
        │
        ▼
  StorageFactory          ← 按 provider 枚举路由到具体实现
        │
        ▼
  StorageProvider         ← 统一接口
        │
        ├── AliyunStorageProviderImpl   (已实现)
        └── QiniuStorageProviderImpl    (预留，未实现)
```

- **StorageProvider**：定义上传、删除、签名 URL 等通用操作
- **StorageFactory**：启动时收集所有 `StorageProvider` 实现，按 `StorageProviderEnum` 索引，默认使用配置中的 `storage.default`
- **StorageModule**：Koin 模块，负责注入 `AliyunStorageConfig` 和默认 provider 枚举

## 目录结构

```
storage/
├── StorageModule.kt              # Koin 依赖注入
├── StorageFactory.kt             # Provider 工厂
├── config/
│   └── AliyunStorageConfig.kt    # 阿里云 OSS 配置
├── dto/
│   └── OssUploadPolicy.kt        # 前端直传凭证 DTO
├── enums/
│   ├── StorageProviderEnum.kt    # 存储服务商
│   └── StorageVisibilityEnum.kt  # 文件可见性
├── provider/
│   ├── StorageProvider.kt        # 核心接口
│   └── impl/
│       └── AliyunStorageProviderImpl.kt
└── utils/
    └── FileUtil.kt               # 文件 key 生成规则
```

## 配置

在 `application.conf` 中配置：

```hocon
storage {
  default = "aliyun"   # 可选值对应 StorageProviderEnum.name，如 ALIYUN

  aliyun {
    endpoint = "oss-cn-hangzhou.aliyuncs.com"
    accessKeyId = "your-access-key-id"
    accessKeySecret = "your-access-key-secret"
    bucket = "your-bucket"
    uploadPrefix = "tmp/"   # 前端直传 policy 允许的 key 前缀
  }
}
```

| 配置项 | 说明 |
|--------|------|
| `storage.default` | 默认存储服务商，未配置时默认为 `ALIYUN` |
| `storage.aliyun.endpoint` | OSS 区域 endpoint |
| `storage.aliyun.accessKeyId` | AccessKey ID |
| `storage.aliyun.accessKeySecret` | AccessKey Secret |
| `storage.aliyun.bucket` | Bucket 名称 |
| `storage.aliyun.uploadPrefix` | 前端直传时 policy 限制的 key 前缀 |

## StorageProvider 接口

| 方法 | 说明 |
|------|------|
| `upload(file, visibility)` | 上传本地文件，返回 OSS object key |
| `uploadStream(inputStream, originalFileName, visibility)` | 通过输入流上传，返回 object key |
| `delete(path)` | 删除指定 key 的对象 |
| `exists(path)` | 判断对象是否存在 |
| `getFullUrl(path)` | 将 key 拼接为完整访问 URL（无签名） |
| `generatePresignedUrl(path)` | 生成带签名的临时访问 URL，默认有效期 1 小时 |
| `move(from, to)` | 在同一 bucket 内移动（重命名）对象 |
| `copy(from, to)` | 在同一 bucket 内复制对象 |
| `removeUrl(url)` | 从完整 URL 中提取 object key |
| `uploadPolicy()` | 生成前端直传 OSS 所需的 Post Policy 凭证 |

## 文件 Key 命名规则

`FileUtil.rename()` 在上传时自动生成 object key，格式为：

```
{visibility}/{mimeTypePrefix}/{yyyyMMdd}/{uuid}.{extension}
```

示例：`public/image/20250605/a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg`

- `visibility`：`private` 或 `public`（来自 `StorageVisibilityEnum.value`）
- `mimeTypePrefix`：根据文件名推断 MIME 类型的主类型（如 `image`、`application`）
- `yyyyMMdd`：上传日期
- `uuid`：UUID v4，保证唯一性

## 枚举

### StorageProviderEnum

| 枚举值 | value | label | 状态 |
|--------|-------|-------|------|
| `ALIYUN` | aliyun | 阿里云 OSS | 已实现 |
| `QINIU` | qiniu | 七牛云 | 预留 |

前端传参时使用枚举 **name**（如 `ALIYUN`），而非 `value`。

### StorageVisibilityEnum

| 枚举值 | value | label |
|--------|-------|-------|
| `PRIVATE` | private | 私有 |
| `PUBLIC` | public | 公有 |

## 使用示例

### 注入与获取 Provider

```kotlin
@Single
class MyService(
    private val storageFactory: StorageFactory,
) {
    private val storage get() = storageFactory.getProvider()

    // 指定服务商
    private val qiniuStorage get() = storageFactory.getProvider(StorageProviderEnum.QINIU)
}
```

### 服务端上传文件

```kotlin
val key = storage.upload(file, StorageVisibilityEnum.PRIVATE)

// 或通过输入流
val key = storage.uploadStream(inputStream, "report.xlsx", StorageVisibilityEnum.PRIVATE)
```

### 生成私有文件访问链接

适用于存储了 object key、需要临时授权访问的场景（如公告附件）：

```kotlin
val signedUrl = storage.generatePresignedUrl(path)
```

### 前端直传 OSS

调用 `uploadPolicy()` 获取 Post Policy 凭证，返回字段：

| 字段 | 说明 |
|------|------|
| `accessId` | AccessKey ID |
| `host` | OSS 上传地址，如 `https://{bucket}.{endpoint}` |
| `policy` | Base64 编码的 policy 文档 |
| `signature` | HMAC-SHA1 签名 |
| `expire` | 过期时间（ISO 8601），默认 2 小时 |
| `prefix` | 允许上传的 key 前缀 |

Policy 限制：
- 单文件大小：0 ~ 1 GB
- key 必须以 `uploadPrefix` 开头

### URL 与 Key 互转

```kotlin
// key → 完整 URL（公开 bucket 可直接访问）
val url = storage.getFullUrl("public/image/20250605/xxx.jpg")

// URL → key
val key = storage.removeUrl("https://bucket.oss-cn-hangzhou.aliyuncs.com/public/image/xxx.jpg")
```

## 扩展新的存储服务商

1. 在 `StorageProviderEnum` 中添加枚举值（若尚未存在）
2. 新增对应的 Config 类（参考 `AliyunStorageConfig`）
3. 实现 `StorageProvider` 接口，并用 `@Single(binds = [StorageProvider::class])` 注册
4. 在 `application.conf` 中添加对应配置块
5. Koin 的 `@ComponentScan` 会自动发现并注入新的 Provider 实现

## 项目内引用

| 模块 | 用途 |
|------|------|
| `OpLogExportService` | 异步导出 Excel 后通过 `uploadStream` 上传至 OSS |
| `NoticeService` | 分页查询时为私有附件生成 `generatePresignedUrl` |
| `TestController` | 存储功能测试入口（`/test/file`） |
