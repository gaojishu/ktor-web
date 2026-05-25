# ktor-web 项目架构文档

> 基于代码扫描自动生成，反映项目当前实际结构（2026-05）。

## 项目概述

`ktor-web` 是一个基于 **Ktor 3.5** 的 Kotlin Web 后端服务，采用 **Netty** 引擎与 **HOCON** 配置（`application.conf`）。项目按 **基础设施（infra）→ 框架插件（plugins）→ 业务特性（feature）** 三层组织，依赖注入使用 **Koin 4 + Koin Annotations** 编译期扫描。

---

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Kotlin | 2.3.21 |
| JVM | JDK Toolchain | 25 |
| Web 框架 | Ktor | 3.5.0（Version Catalog） |
| HTTP 引擎 | Netty | — |
| 依赖注入 | Koin + Koin Annotations | 4.2.1 / 1.0.0 |
| 日志 | Logback | 1.5.32 |
| 序列化 | kotlinx-serialization-json | — |
| 认证 | JWT（auth0 java-jwt） | — |
| 数据库 | PostgreSQL + HikariCP + jOOQ | PG 42.7 / jOOQ 3.21 |
| 数据库迁移 | Flyway | 12.6 |
| 缓存 | Lettuce（连接池）+ Redisson（分布式锁） | 7.5 / 4.4 |
| API 文档 | kotlin-asyncapi-ktor | 3.2.2 |
| 构建 | Gradle + Version Catalog | — |

---

## 目录结构

```
ktor-web/
├── build.gradle.kts              # 主项目构建、依赖、Flyway、jOOQ 源码目录
├── settings.gradle.kts           # 引入 Ktor Version Catalog，包含 jooq-codegen 子模块
├── gradle.properties             # 数据库连接（Flyway CLI / jOOQ 代码生成用）
├── gradle/libs.versions.toml     # 本地依赖版本目录
│
├── jooq-codegen/                 # jOOQ 代码生成子模块
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/example/codegen/
│       ├── CodegenMain.kt
│       └── config/AdminJooqConfig.kt
│
├── api-test/                     # HTTP / WebSocket 接口测试文件
│   ├── ws.http
│   └── http-client.env.json
│
└── src/
    ├── main/
    │   ├── kotlin/com/example/
    │   │   ├── Application.kt              # 程序入口
    │   │   │
    │   │   ├── di/
    │   │   │   └── AppModule.kt            # Koin 根模块（ComponentScan）
    │   │   │
    │   │   ├── plugins/                    # Ktor 框架插件
    │   │   │   ├── PluginLoader.kt         # 插件统一安装入口
    │   │   │   ├── KoinModules.kt          # Koin 安装 + 生命周期管理
    │   │   │   ├── Serialization.kt          # JSON 序列化
    │   │   │   ├── Exception.kt            # 全局异常处理
    │   │   │   ├── Logging.kt              # 请求日志
    │   │   │   ├── RateLimiting.kt         # IP 限流
    │   │   │   ├── Compression.kt          # Gzip 压缩
    │   │   │   ├── ForwardedHeader.kt      # 反向代理头
    │   │   │   ├── RequestValidation.kt    # 请求校验
    │   │   │   └── WebSockets.kt           # WebSocket + /ws 路由
    │   │   │
    │   │   ├── feature/                    # 业务特性层
    │   │   │   ├── RoutesLoader.kt         # 路由统一注册
    │   │   │   ├── admin/                  # 管理端模块
    │   │   │   │   ├── KtorAdminController.kt  # 控制器接口
    │   │   │   │   └── admin/
    │   │   │   │       ├── route/AdminRoute.kt
    │   │   │   │       ├── service/AdminService.kt
    │   │   │   │       ├── repo/AdminRepo.kt
    │   │   │   │       ├── dto/db/AdminDto.kt
    │   │   │   │       ├── enums/AdminStatusEnum.kt
    │   │   │   │       └── converter/AdminStatusConverter.kt
    │   │   │   └── user/                   # 客户端用户模块（示例）
    │   │   │       ├── UserModule.kt
    │   │   │       ├── route/AppUserRoute.kt
    │   │   │       ├── service/AppUserService.kt
    │   │   │       ├── repo/AppUserRepo.kt
    │   │   │       ├── dto/AppUserReq.kt
    │   │   │       └── validation/AppUserValidation.kt
    │   │   │
    │   │   ├── infra/                      # 基础设施层
    │   │   │   ├── database/
    │   │   │   │   ├── DatabaseModule.kt       # HikariCP + Flyway + jOOQ
    │   │   │   │   ├── DatabaseExtensions.kt   # tx / page / softDelete
    │   │   │   │   └── dto/PageQuery.kt, PageResult.kt
    │   │   │   ├── redis/RedisModule.kt          # Lettuce 连接池
    │   │   │   ├── redisson/
    │   │   │   │   ├── RedissonModule.kt
    │   │   │   │   ├── RedissonLock.kt
    │   │   │   │   └── RedissonExtensions.kt
    │   │   │   ├── security/
    │   │   │   │   ├── SecurityModule.kt
    │   │   │   │   ├── SecurityConfigure.kt
    │   │   │   │   ├── JwtConfig.kt
    │   │   │   │   └── JwtService.kt
    │   │   │   └── websocket/WebSocketSessionManager.kt
    │   │   │
    │   │   └── common/                     # 公共工具
    │   │       ├── Ktor.kt                 # ApplicationCall 扩展
    │   │       ├── dto/ApiResult.kt, BaseDto.kt, ValueLabel.kt
    │   │       ├── serializer/             # UUID / LocalDateTime / 金额序列化
    │   │       └── utils/log/, utils/money/
    │   │
    │   └── resources/
    │       ├── application.conf            # 服务、JWT、数据库、Redis 配置
    │       ├── logback.xml
    │       └── db/migration/v10/           # Flyway 迁移脚本
    │
    └── test/kotlin/ServerTest.kt           # 集成测试
```

---

## 启动流程

```
EngineMain.main()
    └── Application.module()
            ├── installPlugins()
            │       ├── configureForwardedHeader()
            │       ├── configureLogging()
            │       ├── configureSerialization()
            │       ├── configureException()
            │       ├── configureRateLimiting()
            │       ├── configureCompression()
            │       ├── configureRequestValidation()
            │       ├── configureWebSockets()
            │       ├── configureKoinModules()   ← 触发基础设施初始化
            │       └── configureSecurity()      ← JWT 认证
            └── installRoutes()
```

### Koin 启动与资源初始化

`configureKoinModules()` 安装 Koin 后，**同步拉取**以下 Bean 以强制建立物理连接：

| Bean | 作用 |
|------|------|
| `HikariDataSource` | PostgreSQL 连接池 |
| `Flyway` | 启动时自动执行数据库迁移 |
| `RedisClient` + `RedisInitMarker` | Redis 连通性检测 |
| `RedissonClient` | 分布式锁客户端 |

### 优雅关闭

监听 `ApplicationStopping` 事件，按序释放：

1. HikariCP 连接池
2. Lettuce RedisClient
3. Redisson 线程池
4. `KoinPlatform.stopKoin()`

---

## 分层架构

```
┌─────────────────────────────────────────────────────────────┐
│  feature/          业务特性层                                │
│  ├── route/        HTTP 路由（或 KtorAdminController）       │
│  ├── service/      业务逻辑                                  │
│  ├── repo/         数据访问（jOOQ DSLContext）               │
│  ├── dto/          请求/响应数据类                           │
│  ├── enums/        枚举 + jOOQ Converter                     │
│  └── validation/   请求校验规则                              │
├─────────────────────────────────────────────────────────────┤
│  plugins/          框架插件层（横切关注点）                   │
│  序列化、异常、日志、限流、压缩、DI、校验、WebSocket、安全    │
├─────────────────────────────────────────────────────────────┤
│  infra/            基础设施层                                │
│  数据库、Redis、Redisson、JWT、WebSocket 会话                │
├─────────────────────────────────────────────────────────────┤
│  di/               依赖注入根模块                            │
│  AppModule → ComponentScan("com.example")                   │
└─────────────────────────────────────────────────────────────┘
```

---

## 依赖注入

采用 **Koin Annotations** 编译期扫描，无需手动注册每个 Bean。

### 根模块

```kotlin
@Module(includes = [DatabaseModule, RedisModule, RedissonModule, SecurityModule])
@ComponentScan("com.example")
class AppModule
```

### 注解约定

| 注解 | 用途 |
|------|------|
| `@Module` | 基础设施模块定义 |
| `@Single` | 单例 Bean（Service、Repo、Route 等） |
| `@Single(createdAtStart = true)` | 启动即初始化（Flyway、Redis 检测、JWT Config） |

业务类只需标注 `@Single`，Koin 编译器自动完成注册与依赖解析。

---

## 路由设计

### API 前缀划分

| 前缀 | 用途 | 认证 | 当前状态 |
|------|------|------|----------|
| `GET /` | 健康检查 | 无 | 返回 `"Hello Word!"` |
| `/api/app` | 客户端 API | `authenticate("app")`（预留） | 空，user 模块未挂载 |
| `/api/admin` | 管理端 API | `authenticate("admin")` | 已实现 Admin 模块 |
| `/ws` | WebSocket 回显 | 无 | 文本帧原样回显 |

### 管理端控制器模式

管理端采用 **接口 + 自动发现** 模式：

```kotlin
interface KtorAdminController {
    fun Route.registerRoutes()
}
```

`RoutesLoader` 通过 `getKoin().getAll<KtorAdminController>()` 获取所有控制器并注册路由。新增管理端模块只需：

1. 实现 `KtorAdminController`
2. 类上标注 `@Single`
3. 在 `registerRoutes()` 中定义路由

### 当前 Admin API

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/api/admin/admin/{id}` | JWT admin | 按 UUID 查询管理员详情 |
| GET | `/api/admin/admin/info` | 白名单（无需 JWT） | 测试接口 |

---

## 安全认证（JWT）

项目配置了 **双 JWT 域**：`admin` 与 `app`，各自独立 secret / audience / 过期时间。

### 配置项（`application.conf`）

```hocon
jwt {
  admin {
    secret, issuer, expiresIn, audience, realm
    excludePaths = ["/api/admin/admin/info", "/admin/login"]
  }
  app {
    secret, issuer, expiresIn, audience, realm
    excludePaths = ["/app/register", "/app/public"]
  }
}
```

### Token 传递方式

1. `Authorization: Bearer <token>` Header（优先）
2. URL Query 参数 `?token=<token>`

### 校验逻辑

- 使用 auth0 `JWTVerifier` 验证签名、issuer、audience
- Payload 必须包含 `userId` claim
- `excludePaths` 支持精确匹配与 `/**` 前缀通配
- 校验失败抛出 `UnauthorizedException` → HTTP 401

### JwtService

```kotlin
fun generateToken(userId: String): String   // 签发 Token
fun makeJwtVerifier(): JWTVerifier         // 构建校验器
fun excludePaths(): List<String>           // 白名单路径
```

---

## 统一响应格式

所有 API 响应与异常均使用 `ApiResult<T>`：

```kotlin
@Serializable
data class ApiResult<T>(
    var code: Int = 0,
    var message: String? = null,
    var data: T? = null,
    var success: Boolean = false
)
```

### 全局异常映射

| 异常类型 | HTTP 状态码 |
|----------|-------------|
| `BusinessException` | 400 |
| `UnauthorizedException` | 401 |
| `RequestValidationException` | 400 |
| `IllegalArgumentException` | 400 |

---

## 数据库

### 连接与迁移

- **连接池**：HikariCP，配置见 `application.conf` → `database.*`
- **ORM**：jOOQ `DSLContext`，通过 Koin 注入
- **迁移**：Koin 启动时 `Flyway.migrate()` 自动执行
- **迁移目录**：`src/main/resources/db/migration/v10/`
- **Schema**：当前仅 `public`

### Flyway 迁移版本

| 版本 | 文件 | 内容 |
|------|------|------|
| V1.0.0 | `V1_0_0__init.sql` | 公共函数 `set_updated_at`、触发器工厂 |
| V1.0.1 | `V1_0_1__admin_init.sql` | admin、permission、role、files、notice、op_log 等表 |
| V1.0.2 | `V1_0_2__admin_init_batch.sql` | 批量初始化数据 |
| V1.0.3 | `V1_0_3__admin_config_init.sql` | config 配置表 |
| V1.0.4–V1.0.10 | `V1_0_4~10__admin_permission_*.sql` | 各模块权限种子数据 |

### 核心数据表

| 表名 | 说明 |
|------|------|
| `admin` | 管理员（UUID v7 主键，软删除） |
| `permission` | 权限树（菜单/按钮） |
| `admin_permission` | 管理员-权限关联 |
| `role` | 角色 |
| `files` / `files_category` | 文件存储 |
| `notice` | 公告 |
| `op_log` | 操作日志 |
| `config` | 键值配置（JSON value） |

### jOOQ 扩展

| 扩展 | 说明 |
|------|------|
| `dsl.tx { }` | 事务包装 |
| `selectActiveFrom(table)` | 自动过滤 `deleted_at IS NULL` |
| `softDelete(table, condition)` | 软删除（设置 deleted_at） |
| `page()` / `pageInto()` | 分页查询 |

### jOOQ 代码生成

```bash
./gradlew codegen
# 或
./gradlew :jooq-codegen:run
```

- 配置读取 `gradle.properties` 中的 `db.*`
- 生成目录：`jooq-codegen/build/generated-sources/jooq/`
- 包名：`com.example.jooq.generate`
- 主项目 `sourceSets` 已引用该目录
- 自定义类型映射见 `AdminJooqConfig`（如 `AdminStatusEnum`）

### Gradle Flyway 任务

```bash
./gradlew flywayMigrate    # 手动执行迁移
./gradlew flywayInfo       # 查看迁移状态
```

连接参数读取 `gradle.properties` 中的 `db.url`、`db.user`、`db.pass`、`db.schemas`。

---

## Redis

| 组件 | 模块 | 用途 |
|------|------|------|
| Lettuce + 连接池 | `RedisModule` | 通用 KV 操作 |
| Redisson | `RedissonModule` | 分布式锁等高级特性 |
| `RedissonLock` | — | `withLock(key) { }` 封装 |

共用 `application.conf` → `redis.*` 配置（host、port、password、database、pool）。

---

## 序列化

`configureSerialization()` 全局 JSON 配置：

- `encodeDefaults = true` — 输出默认值字段
- `explicitNulls = true` — 保留 null 字段
- 全局 `UUID` → 字符串序列化（`UUIDSerializer`）
- `BaseDto` 中 `LocalDateTime` 使用自定义 `LocalDateTimeSerializer`

---

## 限流

| 名称 | 规则 | 键 |
|------|------|-----|
| `per_ip_limit` | 100 次 / 60 秒 | 客户端 IP |

在路由中通过 `rateLimit(RateLimitName("per_ip_limit")) { }` 使用。

---

## WebSocket

- 路径：`/ws`
- 心跳：15 秒 ping / 15 秒超时
- 当前实现：文本帧回显（Echo）
- `WebSocketSessionManager` 预留在线会话管理能力

---

## 公共 DTO 基类

```kotlin
@Serializable
abstract class BaseDto {
    @Contextual var id: UUID?
    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
}
```

业务 DTO（如 `AdminDto`）继承 `BaseDto`，叠加业务字段。

---

## 新增业务模块指南

### 管理端模块

```
feature/admin/<module>/
├── route/XxxRoute.kt       # 实现 KtorAdminController + @Single
├── service/XxxService.kt   # @Single
├── repo/XxxRepo.kt         # @Single，注入 DSLContext
├── dto/db/XxxDto.kt
└── enums/ + converter/     # 如需 jOOQ 枚举映射
```

无需修改 Koin 注册或 RoutesLoader — ComponentScan 与 `getAll<KtorAdminController>()` 自动发现。

### 客户端模块

1. 在 `feature/<module>/` 下创建 route / service / repo / dto / validation
2. 编写 `XxxModule.kt` 挂载路由
3. 在 `RoutesLoader.kt` 的 `/api/app` 下调用 `xxxModule()`
4. 在 `RequestValidation.kt` 中注册校验规则
5. Service / Repo 标注 `@Single` 即可

---

## 配置说明

### `application.conf`（运行时）

```hocon
ktor.deployment.port = 8001
ktor.deployment.host = "0.0.0.0"

database { jdbcUrl, username, password, schemas, pool 参数 }
redis    { host, port, password, database, pool 参数 }
jwt      { admin { ... }, app { ... } }
```

### `gradle.properties`（构建时）

```properties
db.url=jdbc:postgresql://localhost:5432/test
db.user=postgres
db.pass=***
db.driver=org.postgresql.Driver
db.schemas=public
```

---

## 快速开始

### 环境要求

- JDK 25+
- PostgreSQL
- Redis

### 运行

```bash
./gradlew run
```

服务地址：`http://localhost:8001`

### 测试

```bash
./gradlew test
```

### 构建

```bash
./gradlew build
```

---

## 模块完成度

| 模块 | 状态 | 备注 |
|------|------|------|
| 基础设施（DB / Redis / JWT） | ✅ 完成 | Koin 生命周期管理 |
| 管理端 Admin | 🚧 进行中 | 路由/Repo 骨架已有，CRUD 待完善 |
| 客户端 User | 🚧 骨架 | 路由未挂载到 `/api/app` |
| 权限种子数据 | ✅ 完成 | V1.0.4–V1.0.10 迁移脚本 |
| WebSocket | ✅ 基础 | Echo 服务 |
| AsyncAPI 文档 | 📦 依赖已引入 | 待集成 |

---

## 安全提示

- 本地敏感配置（数据库密码、JWT secret 等）应通过环境变量或外部配置覆盖，**不要提交到版本库**
- 生产环境务必更换 `application.conf` 中的默认 JWT secret
- Flyway `cleanDisabled = true`（运行时），Gradle Flyway 插件允许 clean（开发用）
