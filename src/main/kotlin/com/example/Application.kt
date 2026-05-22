package com.example

import com.example.feature.installRoutes
import com.example.infra.database.DatabaseManager
import com.example.infra.redisson.RedissonManager
import com.example.infra.redis.RedisManager
import com.example.plugins.installPlugins
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //初始化数据库连接
    DatabaseManager.init(environment.config)

    //初始化redis连接
    RedisManager.init(environment.config)
    RedissonManager.init(environment.config)

    installPlugins()
    installRoutes()

    monitor.subscribe(ApplicationStopping) {
        DatabaseManager.close()
        RedisManager.close()
        RedissonManager.close()
    }
}
