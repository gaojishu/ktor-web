# ktor-web

基于 [Ktor 3.4](https://ktor.io/) 的 Kotlin Web 服务项目，使用 Netty 引擎与 YAML 配置，采用 **插件（Plugins）+ 业务模块（Modules）** 的分层启动方式。

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin 2.3.21（JVM 21） |
| Web 框架 | Ktor 3.4.0 |
| HTTP 引擎 | Netty |
| 日志 | Logback 1.5.21 |
| 序列化 | kotlinx-serialization-json 1.11.0 |
| API 文档 | kotlin-asyncapi-ktor 3.2.1 |
| 构建工具 | Gradle（Version Catalog） |

## 目录结构

```
ktor-web/
├── build.gradle.kts              # 项目构建与依赖配置
├── settings.gradle.kts           # Gradle 设置，引入 Ktor Version Catalog
├── gradle.properties             # Gradle / Kotlin 构建参数
├── gradle/
│   ├── libs.versions.toml        # 本地依赖版本目录
│   └── wrapper/                  # Gradle Wrapper
├── gradlew / gradlew.bat         # Gradle 启动脚本
├── .gitignore
│
└── src/
    ├── main/
    │   ├── kotlin/com/example/
    │   │   ├── Application.kt           # 程序入口，注册插件与模块
    │   │   ├── plugins/
    │   │   │   └── PluginLoader.kt      # 框架级插件安装（路由、序列化、安全等）
    │   │   └── modules/
    │   │       └── ModulesLoader.kt     # 业务模块注册入口
    │   │
    │   └── resources/
    │       ├── application.yaml         # Ktor 服务配置（端口、模块入口）
    │       └── logback.xml              # 日志输出配置
    │
    └── test/
        └── kotlin/
            └── ServerTest.kt            # 集成测试（testApplication）
```

## 核心文件说明

### `Application.kt`

应用主入口，通过 `EngineMain` 启动 Netty 引擎，并在 `module()` 中依次调用：

1. `installPlugins()` — 安装框架基础设施（CORS、序列化、监控等）
2. `installModules()` — 注册各业务模块路由与服务

### `plugins/PluginLoader.kt`

框架级插件的统一安装点。当前为空实现，后续可在此添加：

- 路由（Routing）
- 序列化（Serialization）
- 安全 / JWT（Security）
- 监控与日志（Monitoring）

### `modules/ModulesLoader.kt`

业务模块的统一注册点。当前为空实现，后续按业务垂直切分模块（如 auth、order 等），在此集中调用各模块的 `installXxxModule()`。

### `application.yaml`

```yaml
ktor:
  deployment:
    port: 8001
    host: 0.0.0.0
  application:
    modules:
      - com.example.ApplicationKt.module
```

服务默认监听 `0.0.0.0:8001`，模块入口为 `ApplicationKt.module`。

## 快速开始

### 环境要求

- JDK 21+
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

构建产物位于 `build/` 目录。

## 规划架构（待实现）

以下为推荐的后续目录布局，便于按业务扩展：

```
src/main/kotlin/com/example/
│
|-- api
|   |-- admin
|   |-- api
|
├── infra/task/                    # 异步任务基础设施
│   ├── TaskDispatcher.kt
│   ├── TaskProcessor.kt
│   └── TaskStatus.kt
│
├── plugins/                       # 框架插件
│   ├── Routing.kt
│   ├── Security.kt
│   ├── Serialization.kt
│   └── Monitoring.kt
│
├── modules/                       # 业务模块（垂直切分）
│   ├── auth/
│   │   ├── AuthModule.kt
│   │   ├── AuthRouting.kt
│   │   └── UserService.kt
│   └── order/
│       ├── OrderModule.kt
│       ├── OrderRouting.kt
│       └── OrderService.kt
│
├── database/
│   ├── DbContext.kt
│   └── DbExtensions.kt
│
└── Application.kt
```

## 配置与安全

- 本地敏感配置（数据库密码、JWT 密钥等）应放在 `.env` 或环境变量中，**不要提交到版本库**。
- `.gitignore` 已忽略 `.env`、`.env.local` 及 IDE 缓存目录。
- 生产环境部署时，请通过环境变量或外部配置覆盖 `application.yaml` 中的默认值。
