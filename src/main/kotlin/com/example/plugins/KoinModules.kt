package com.example.plugins

import com.example.di.AppModule
import com.example.infra.redis.RedisInitMarker
import com.example.infra.websocket.WebSocketSessionManager
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.lettuce.core.RedisClient
import org.flywaydb.core.Flyway
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.plugin.module.dsl.module
import org.redisson.api.RedissonClient

fun Application.configureKoinModules() {

    install(Koin) {
        slf4jLogger()
        // 载入应用基础动态配置
        modules(org.koin.dsl.module {
            single { environment.config }
        })


        module<AppModule>()
    }

    log.info("【系统启动】正在绕过 K2 编译器惰性，强制激活基础设施...")

    // 同步非惰性拉取，打通启动期物理 TCP 连接
    val db = this.get<HikariDataSource>()
    this.get<Flyway>()
    val redis = this.get<RedisClient>()
    this.get<RedisInitMarker>()
    val redisson = this.get<RedissonClient>()
    this.get<WebSocketSessionManager>()


    // 🎯 更正 2：全面切换为 Ktor 3.x 标准最高优先级的 Stopping 监听
    monitor.subscribe(ApplicationStopping) {
        log.info("【生命周期】Ktor 核心已成功拦截终止信号（SIGINT），开始同步注销基础设施连接池...")

        // 1. 🎯 核心修复：物理注销数据库连接池
        // 通过 runCatching 杜绝因 Koin 内部卸载异常而导致 close() 被吞掉
        runCatching {
            log.info("【生命周期】db.close")
            db.close() // ⬅️ 原地强行轰炸出 HikariCP - Shutdown completed 日志
        }.onFailure { log.error("数据库释放失败: ", it) }

        // 2. 🎯 核心修复：物理注销 Redis (Lettuce) 连接池
        runCatching {
            log.info("💥 redis close") // ⬅️ 完美带回你旧代码里的自定义日志
            redis.close()
        }.onFailure { log.error("Redis 释放失败: ", it) }

        // 3. 🎯 核心修复：物理注销 Redisson 线程池注销...
        runCatching {
            log.info("💥 Redisson shutdown") // ⬅️ 完美带回你旧代码里的自定义日志
            redisson.shutdown() // ⬅️ 强行调用物理 shutdown 清空中间件线程池
        }.onFailure { log.error("Redisson 释放失败: ", it) }

        // 4. 🎯 核心修复：把 stopKoin() 挪到所有连接池被实体关闭的最后一关
        // 改用新版编译器插件专属的跨平台卸载函数，100% 抹平内存残留
        runCatching {
            org.koin.mp.KoinPlatform.stopKoin()
            log.info("【生命周期】Koin 静态全局依赖图安全完全卸载！")
        }.onFailure { log.error("KoinPlatform.stopKoin() 执行遭遇拦截: ", it) }
    }
}
