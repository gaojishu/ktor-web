package com.example

import com.example.feature.installRoutes
import com.example.plugins.installPlugins
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installPlugins()
    installRoutes()
}
