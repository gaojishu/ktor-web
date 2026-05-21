package com.example

import com.example.database.DatabaseFactory
import com.example.modules.installModules
import com.example.plugins.installPlugins
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //初始化数据库连接
    DatabaseFactory.init(environment.config)

    installPlugins()
    installModules()

}
