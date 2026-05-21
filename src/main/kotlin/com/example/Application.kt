package com.example

import com.example.database.DatabaseFactory
import com.example.database.RedisPoolManager
import com.example.modules.installModules
import com.example.plugins.installPlugins
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //初始化数据库连接池
    DatabaseFactory.init(environment.config)

    //初始化redis连接池
    RedisPoolManager.init(environment.config)

    installPlugins()
    installModules()

    monitor.subscribe(ApplicationStopping) {
        DatabaseFactory.close()
        RedisPoolManager.close()
    }
}
