# Storage 模块

对象存储抽象层，通过 `StorageFactory` 统一管理多个云存储服务商，业务代码只依赖 `StorageProvider` 接口。

## 目录结构

```
storage/
├── StorageFactory.kt          # 存储工厂：根据枚举获取服务商实例
├── StorageModule.kt           # Koin 注解模块：启动时自动初始化
├── StorageProvider.kt         # 存储能力接口
├── enums/
│   └── StorageProviderEnum.kt # 服务商枚举
├── impl/
│   └── AliyunStorageProviderImpl.kt  # 阿里云 OSS 实现
└── README.md
```

## 架构概览

```
ApplicationConfig
       │
       ▼
StorageModule (@Module)
       │
       ├── provideAliyunStorageProvider()
       ├── provideStorageProviders()
       ├── provideDefaultStorageProvider()
       └── provideStorageFactory()  ← createdAtStart = true
       │
       ▼
StorageFactory (Koin 单例) ──► StorageProvider
                                       │
                                       ├── upload / uploadStream
                                       ├── delete / exists / move / copy
                                       ├── generatePresignedUrl / getFullUrl
                                       └── ...
```

## 快速开始

### 1. 配置

在 `application.conf` 中增加 storage 配置（字段名需与 `StorageFactory.initialize` 一致）：

```hocon
storage {
  # 可选，默认 ALIYUN；取值对应 StorageProviderEnum 的 name（大写）
  default = "ALIYUN"

  aliyun {
    endpoint = "oss-cn-hangzhou.aliyuncs.com"
    accessKeyId = "your-access-key-id"
    accessKeySecret = "your-access-key-secret"
    bucket = "your-bucket-name"
  }
}
```

### 2. 启动时初始化

Storage 通过 Koin 注解在应用启动时自动初始化，无需手动调用 `initialize`：

- `StorageModule` 已在 `AppModule.includes` 中注册
- `provideStorageFactory` 设置了 `createdAtStart = true`，Ktor 启动时会立即创建
- `KoinModules.kt` 中也会 `get<StorageFactory>()` 以确保启动期激活

### 3. 业务中使用

**方式一：构造函数注入（推荐）**

```kotlin
@Single
class FileService(private val storageFactory: StorageFactory) {
    fun upload(file: File) {
        val url = storageFactory.getProvider().upload(file, visibility = "public")
    }
}
```

**方式二：运行时获取**

```kotlin
val storageFactory = get<StorageFactory>()
val provider = storageFactory.getProvider(StorageProviderEnum.ALIYUN)
```

## StorageFactory

| 成员 | 说明 |
|------|------|
| `getProvider(provider?)` | 获取指定服务商实例；`provider` 为 `null` 时返回默认服务商 |

由 `StorageModule` 在启动时注入 `providers` 与 `defaultProvider`，无需手动初始化。

## StorageModule

| 方法 | 说明 |
|------|------|
| `provideAliyunStorageProvider` | 读取 `storage.aliyun.*` 配置，创建阿里云实现 |
| `provideStorageProviders` | 汇总所有已注册的服务商 Map |
| `provideDefaultStorageProvider` | 读取 `storage.default` 配置 |
| `provideStorageFactory` | 组装 `StorageFactory`，`createdAtStart = true` |

## StorageProvider 接口

| 方法 | 说明 |
|------|------|
| `generateUploadCredential(fileName)` | 生成前端直传凭证 |
| `upload(file, visibility)` | 上传本地文件，返回访问路径或 URL |
| `uploadStream(inputStream, originalFileName)` | 流式上传 |
| `delete(path)` | 删除对象 |
| `exists(path)` | 判断对象是否存在 |
| `generatePresignedUrl(path)` | 生成预签名访问 URL |
| `getFullUrl(path)` | 获取完整访问 URL |
| `move(from, to)` | 移动对象 |
| `copy(from, to)` | 复制对象 |
| `uploadPolicy()` | 获取上传策略（如 Post Policy） |
| `rename(objectName, visibility)` | 重命名对象 |
| `removeUrl(url)` | 从 URL 中提取存储路径 |

## 服务商枚举

`StorageProviderEnum` 当前支持：

| 枚举值 | value | label | 实现状态 |
|--------|-------|-------|----------|
| `ALIYUN` | `aliyun` | 阿里云 OSS | 已注册，方法待实现 |
| `QINIU` | `qiniu` | 七牛云 | 未注册 |

前端下拉选项可通过 `StorageProviderEnum.getAllValueLabel()` 获取，返回的 `value` 为枚举 `name`（如 `ALIYUN`），便于后端直接解析。

## 扩展新服务商

1. 在 `StorageProviderEnum` 中新增枚举项
2. 在 `impl/` 下实现 `StorageProvider` 接口
3. 在 `StorageModule.provideStorageProviders` 中注册到 Map
4. 在 `AppModule.includes` 中确保已包含 `StorageModule::class`（已默认包含）

## 实现状态

`AliyunStorageProviderImpl` 当前所有方法均为 `TODO`，尚未接入实际 OSS 操作。使用前需先完成具体实现。

## 注意事项

- `StorageFactory` 使用内存 `Map` 缓存 provider 实例，生命周期与应用进程一致
- `storage.default` 配置值会被转为大写后通过 `StorageProviderEnum.valueOf` 解析，需与枚举 `name` 一致（如 `ALIYUN`）
- 若 `demo.application.conf` 中的 storage 结构与上述示例不一致，请以 `StorageFactory.kt` 中实际读取的 key 为准并同步修改配置
