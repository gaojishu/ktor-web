package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO // 设置日志级别
        // 过滤不需要记录的请求（如健康检查接口）
        filter { call -> call.request.path().startsWith("/") }
    }
}