package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSockets() {
    install(WebSockets) {
        pingPeriod = 15.seconds // 心跳检测周期
        timeout = 15.seconds    // 超时时间
        maxFrameSize = Long.MAX_VALUE       // 最大帧大小
        masking = false
    }

}
