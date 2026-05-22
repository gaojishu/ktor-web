# ktor-web

基于 [Ktor 3.4](https://ktor.io/) 的 Kotlin Web 服务项目，使用 Netty 引擎与 YAML 配置。采用 **基础设施（infra）+ 框架插件（plugins）+ 业务特性（feature）** 的分层架构，依赖注入使用 Koin。

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin 2.3.21（JVM 21） |
| Web 框架 | Ktor 3.4.3 |
| HTTP 引擎 | Netty |
| 依赖注入 | Koin 4.2.1 |
| 日志 | Logback 1.5.32 |
| 序列化 | kotlinx-serialization-json |
| 数据库 | PostgreSQL + HikariCP + jOOQ 3.21 |
| 数据库迁移 | Flyway 12.6 |
| 缓存 | Lettuce（连接池）+ Redisson（分布式锁） |
| API 文档 | kotlin-asyncapi-ktor 3.2.2 |
| 构建工具 | Gradle（Version Catalog） |

## 目录结构

```
ktor-web/
├── build.gradle.kts              # 主项目构建、依赖与 Flyway 配置
├── settings.gradle.kts           # Gradle 设置，引入 Ktor Version Catalog
├── gradle.properties             # 构建参数 + 数据库连接（Flyway / jOOQ 用）
├── gradle/
│   ├── libs.versions.toml        # 本地依赖版本目录
│   └── wrapper/
├── jooq-codegen/                 # jOOQ 代码生成子模块
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/example/codegen/CodegenMain.kt
│
└── src/
    ├── main/
    │   ├── kotlin/com/example/
    │   │   ├── Application.kt              # 程序入口与 module() 启动流程
    │   │   │
    │   │   ├── di/
    │   │   │   └── Koin.kt                 # 全局 Koin 模块聚合
    │   │   │
    │   │   ├── plugins/                    # 框架级 Ktor 插件
    │   │   │   ├── PluginLoader.kt         # 插件统一安装入口
    │   │   │   ├── Serialization.kt        # JSON 序列化
    │   │   │   ├── Exception.kt            # 全局异常处理
    │   │   │   ├── Logging.kt              # 请求日志
    │   │   │   ├── RateLimiting.kt         # IP 限流
    │   │   │   ├── Compression.kt          # Gzip 压缩
    │   │   │   ├── ForwardedHeader.kt      # 反向代理头解析
    │   │   │   ├── KoinModules.kt          # Koin 插件安装
    │   │   │   ├── RequestValidation.kt    # 请求校验插件
    │   │   │   └── WebSockets.kt           # WebSocket 配置与 /ws 路由
    │   │   │
    │   │   ├── feature/                    # 业务特性（按领域垂直切分）
    │   │   │   ├── RoutesLoader.kt         # 路由统一注册入口
    │   │   │   └── user/                   # 用户模块示例
    │   │   │       ├── UserModule.kt       # 模块路由挂载
    │   │   │       ├── route/AppUserRoute.kt
    │   │   │       ├── service/AppUserService.kt
    │   │   │       ├── repo/UserRepo.kt
    │   │   │       ├── dto/AppUserReq.kt
    │   │   │       └── validation/AppUserValidation.kt
    │   │   │
    │   │   └── infra/                      # 基础设施层
    │   │       ├── database/
    │   │       │   ├── DatabaseManager.kt      # HikariCP + Flyway + jOOQ DSL
    │   │       │   ├── DatabaseKoinModule.kt   # DSLContext 注入
    │   │       │   └── DatabaseExtensions.kt   # 事务扩展 tx {}
    │   │       ├── redis/
    │   │       │   └── RedisManager.kt         # Lettuce 连接池
    │   │       ├── redisson/
    │   │       │   ├── RedissonManager.kt      # Redisson 客户端
    │   │       │   ├── RedissonLock.kt         # 分布式锁
    │   │       │   └── RedissonExtensions.kt   # redissonLock {} 扩展
    │   │       ├── websocket/
    │   │       │   └── WebSocketSessionManager.kt  # 在线会话管理
    │   │       ├── dto/
    │   │       │   └── ApiResult.kt            # 统一 API 响应体
    │   │       └── log/
    │   │           └── Log.kt                  # log 扩展属性
    │   │
    │   └── resources/
    │       ├── application.yaml          # Ktor 服务、数据库、Redis 配置
    │       ├── logback.xml               # 日志输出配置
    │       └── db/migration/             # Flyway 迁移脚本
    │           └── v1/
    │               ├── V0.0.1__init.sql
    │               └── V1.0.x__*.sql
    │
    └── test/
        └── kotlin/ServerTest.kt          # 集成测试（testApplication）
```

## 启动流程

应用通过 `EngineMain` 启动 Netty，`application.yaml` 中配置的 `com.example.ApplicationKt.module` 为模块入口。

```
EngineMain.main()
    └── Application.module()
            ├── DatabaseManager.init()     # HikariCP 连接池 + Flyway 迁移 + jOOQ DSL
            ├── RedisManager.init()        # Lettuce 连接池
            ├── RedissonManager.init()     # Redisson 客户端
            ├── installPlugins()           # 安装框架插件
            ├── installRoutes()            # 注册业务路由
            └── monitor.subscribe(ApplicationStopping)  # 优雅关闭资源
```

### 插件安装顺序（`PluginLoader.kt`）

| 顺序 | 插件 | 说明 |
|------|------|------|
| 1 | Serialization | JSON ContentNegotiation |
| 2 | Exception | StatusPages 全局异常 → `ApiResult` |
| 3 | Logging | CallLogging 请求日志 |
| 4 | RateLimiting | 按 IP 限流（100 次/60 秒） |
| 5 | ForwardedHeader | 反向代理 X-Forwarded 头 |
| 6 | Compression | Gzip 响应压缩 |
| 7 | Koin | 依赖注入容器 |
| 8 | RequestValidation | 请求体校验 |
| 9 | WebSockets | WebSocket 协议 + `/ws` 路由 |

## 分层架构

```
┌─────────────────────────────────────────────────┐
│  feature/          业务特性层（路由、服务、仓储）   │
│  ├── route/        HTTP 路由定义                  │
│  ├── service/      业务逻辑                       │
│  ├── repo/         数据访问（jOOQ）               │
│  ├── dto/          请求/响应数据类                 │
│  └── validation/   请求校验规则                    │
├─────────────────────────────────────────────────┤
│  plugins/          框架插件层（横切关注点）          │
│  序列化、异常、日志、限流、压缩、DI、校验、WS       │
├─────────────────────────────────────────────────┤
│  infra/            基础设施层                      │
│  数据库、Redis、Redisson、WebSocket、DTO、日志     │
├─────────────────────────────────────────────────┤
│  di/               依赖注入配置                     │
│  Koin 模块聚合与 Bean 注册                         │
└─────────────────────────────────────────────────┘
```

### 路由约定

`RoutesLoader.kt` 按 API 前缀划分端：

| 前缀 | 用途 | 当前状态 |
|------|------|----------|
| `/` | 健康检查 | `GET /` → "Hello Word!" |
| `/api/app` | 客户端 API | 已挂载 `userModule()` |
| `/api/admin` | 管理端 API | 预留，待扩展 |
| `/ws` | WebSocket | 回显服务（在 `WebSockets.kt` 中定义） |

示例：`GET /api/app/user/{id}` 由 `AppUserRoute` 提供，并受 `per_ip_limit` 限流保护。

### 统一响应格式

所有异常与业务响应均使用 `ApiResult<T>`：

```kotlin
@Serializable
data class ApiResult<T>(
    val code: Int = 0,
    val message: String? = null,
    val data: T? = null,
    val success: Boolean = false
)
```

全局异常处理（`Exception.kt`）捕获 `BusinessException`、`RequestValidationException`、`IllegalArgumentException`，统一返回 HTTP 400 + `ApiResult`。

## 依赖注入

Koin 模块在 `di/Koin.kt` 中聚合：

```kotlin
val allModules = listOf(
    databaseKoinModule,          // DSLContext → DatabaseManager.dsl
    module { single { UserRepo(get()) } },
    module { single { AppUserService(get()) } },
)
```

路由层通过 `by inject<T>()` 获取服务实例。新增业务模块时，在此追加对应的 `single` / `factory` 注册即可。

## 数据库

### 连接与迁移

- **连接池**：HikariCP，配置项见 `application.yaml` 的 `database.*`
- **ORM**：jOOQ `DSLContext`，通过 `DatabaseManager.dsl` 全局访问
- **迁移**：应用启动时 `DatabaseManager` 自动执行 Flyway 迁移（`resources/db/migration/`）
- **Schema**：`admin`、`public` 双 schema
- **事务**：使用 `dsl.tx { ... }` 扩展函数

### Gradle Flyway 任务

`build.gradle.kts` 中配置了 Flyway 插件，连接参数读取自 `gradle.properties`：

```bash
./gradlew flywayMigrate    # 手动执行迁移
./gradlew flywayInfo       # 查看迁移状态
```

### jOOQ 代码生成

独立子模块 `jooq-codegen`，从数据库 schema 生成 Kotlin 代码：

```bash
./gradlew :jooq-codegen:run
```

生成产物位于 `jooq-codegen/build/generated-sources/jooq/`，包名 `com.example.jooq.generate`。

## Redis

| 组件 | 用途 | 访问方式 |
|------|------|----------|
| `RedisManager` | 通用 KV 操作 | `RedisManager.execute { conn -> ... }` 连接池借还 |
| `RedissonManager` | 分布式锁等高级特性 | `RedissonManager.getClient()` |
| `RedissonLock` | 锁封装 | `redissonLock("key") { ... }` |

两者共用 `application.yaml` 中 `redis.*` 配置。

## 新增业务模块指南

以新增 `order` 模块为例：

1. **创建目录**

```
feature/order/
├── OrderModule.kt
├── route/OrderRoute.kt
├── service/OrderService.kt
├── repo/OrderRepo.kt
├── dto/OrderReq.kt
└── validation/OrderValidation.kt
```

2. **定义模块路由**（`OrderModule.kt`）

```kotlin
fun Route.orderModule() {
    orderRoute()
}
```

3. **注册路由**（`RoutesLoader.kt`）

```kotlin
route("/api/app") {
    userModule()
    orderModule()   // 新增
}
```

4. **注册 Koin Bean**（`di/Koin.kt`）

```kotlin
module {
    single { OrderRepo(get()) }
    single { OrderService(get()) }
}
```

5. **注册校验规则**（`RequestValidation.kt`）

```kotlin
install(RequestValidation) {
    AppUserValidation.register(this)
    OrderValidation.register(this)   // 新增
}
```

## 配置说明

### `application.yaml`

```yaml
ktor:
  deployment:
    port: 8001
    host: 0.0.0.0
  application:
    modules:
      - com.example.ApplicationKt.module

database:    # HikariCP 连接池参数
redis:       # Redis / Redisson 连接与连接池参数
```

### `gradle.properties`

```properties
db.url=jdbc:postgresql://localhost:5432/test
db.user=postgres
db.pass=***
db.driver=org.postgresql.Driver
db.schemas=public,admin
```

## 快速开始

### 环境要求

- JDK 21+
- PostgreSQL
- Redis
- 无需预装 Gradle（项目自带 Wrapper）

### 运行

```bash
./gradlew run
```

启动后访问：`http://localhost:8001`

### 测试

```bash
./gradlew test
```

### 构建

```bash
./gradlew build
```

## 配置与安全

- 本地敏感配置（数据库密码、Redis 密码等）应放在 `.env` 或环境变量中，**不要提交到版本库**。
- `.gitignore` 已忽略 `.env`、`.env.local` 及 IDE 缓存目录。
- 生产环境部署时，请通过环境变量或外部配置覆盖 `application.yaml` 中的默认值。
- `logs/` 目录为运行时日志输出，不应纳入版本控制。
