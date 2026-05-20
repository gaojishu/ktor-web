```text
src/main/kotlin/com/example/
│
├── infra/task/                    # 1. 异步任务基础设施层（全项目通用）
│   ├── TaskDispatcher.kt          # 任务分发器（基于协程 Channel）
│   ├── TaskProcessor.kt           # 任务处理器接口
│   └── TaskStatus.kt              # 任务状态枚举 (PENDING, RUNNING, SUCCESS, FAILED)
│
├── plugins/                      # 1. 框架基础设施插件配置
│   ├── Routing.kt                # 总路由分发
│   ├── Security.kt               # 鉴权与安全设置 (JWT)
│   ├── Serialization.kt          # 序列化配置 (Jackson/kotlinx.serialization)
│   └── Monitoring.kt             # 日志与指标监控 (Logback/Micrometer)
│
├── modules/                      # 2. 业务功能模块 (按业务垂直切分)
│   ├── auth/                     # 用户认证业务
│   │   ├── AuthModule.kt         # 模块注册入口 (Application 扩展函数)
│   │   ├── AuthRouting.kt        # 登录、注册路由端点
│   │   └── UserService.kt        # 业务逻辑层
│   └── order/                    # 订单业务
│       ├── OrderModule.kt
│       ├── OrderRouting.kt
│       └── OrderService.kt
│
├── database/
│   ├── DbContext.kt      # jOOQ DSLContext 的配置与线程池管理
│   └── DbExtensions.kt   # 针对 jOOQ 的 Kotlin 扩展工具函数                # 数据库表 Schema 定义
│
├── Application.kt                # 4. 程序主入口 (初始化引擎与模块)
└── resources/
    └── application.yaml          # 全局配置文件

```