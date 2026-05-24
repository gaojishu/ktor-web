package com.example.plugins

import com.example.common.utils.log.log
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import io.ktor.server.websocket.*
import io.ktor.websocket.send
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSockets() {
    install(WebSockets) {
        pingPeriod = 15.seconds // 心跳检测周期
        timeout = 15.seconds    // 超时时间
        maxFrameSize = Long.MAX_VALUE       // 最大帧大小
        masking = false
    }

    routing {
        webSocket("/ws") {
            log.info("WebSocket 🟢 客户端已连接")
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    send(text)
                }
            }
        }
    }
}
