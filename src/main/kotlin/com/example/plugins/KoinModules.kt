package com.example.plugins

import com.example.di.AppModule
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.application.log
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.plugin.module.dsl.module

fun Application.configureKoinModules() {

    install(Koin) {
        slf4jLogger()
        // 载入应用基础动态配置
        modules(org.koin.dsl.module {
            single { environment.config }
        })


        module<AppModule>()
    }

    // 🎯 更正 2：全面切换为 Ktor 3.x 标准最高优先级的 Stopping 监听
    monitor.subscribe(ApplicationStopping) {

        runCatching {
            org.koin.mp.KoinPlatform.stopKoin()
            log.info("【生命周期】Koin 静态全局依赖图安全完全卸载！")
        }.onFailure { log.error("KoinPlatform.stopKoin() 执行遭遇拦截: ", it) }
    }
}
