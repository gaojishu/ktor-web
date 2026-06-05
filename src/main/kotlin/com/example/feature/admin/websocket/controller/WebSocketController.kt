package com.example.feature.admin.websocket.controller

import com.example.common.utils.log.log
import com.example.feature.KtorAdminController
import com.example.infra.security.CurrentUser
import com.example.infra.websocket.WebSocketSessionManager
import io.ktor.server.auth.authentication
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import org.koin.core.annotation.Single

@Single
class WebSocketController: KtorAdminController {
    override fun Route.registerRoutes() {
        webSocket("/ws") {
            log.info("WebSocket 🟢 客户端已连接")

            // 1. 🎯 从握手请求的查询参数中获取 userId (例如: /ws?userId=10001)
            // 如果前端使用的是 Token，此处可改为 call.request.queryParameters["token"] 并解密出 userId
            val auth = call.authentication.principal<CurrentUser>()

            // 2. 🎯 获取 Ktor 为当前 TCP 连接自动生成的唯一会话 ID
            val sessionId = call.parameters["id"] ?: this.hashCode().toString()

            if (auth?.id == null) {
                log.warn("WebSocket 🔴 连接拒绝：未提供有效的 userId")
                close(io.ktor.websocket.CloseReason(io.ktor.websocket.CloseReason.Codes.VIOLATED_POLICY, "Missing userId"))
                return@webSocket
            }
            val userId = auth.id.toString()
            try {
                // 3. 🎯 将当前会话（this）与 userId 绑定到管理器中
                WebSocketSessionManager.addSession(userId, this)
                log.info("WebSocket 🟢 客户端已连接 | 用户ID: $userId | 会话ID: $sessionId")

                // 4. 进入消息监听循环
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        // 收到前端消息时的处理逻辑（如心跳应答，或者直接 echo 回去）
                        if (text == "ping" || text == "PING") {
                            send(Frame.Text("pong"))
                        }
                    }
                }
            } catch (e: Exception) {
                log.error("WebSocket 会话期间发生异常 [User: $userId]: ${e.message}")
            } finally {
                // 5. 🎯 极其重要：当客户端断开连接（离开循环）时，必须注销，释放内存
                WebSocketSessionManager.removeSession(auth.id.toString())
                log.info("WebSocket 🔴 客户端已断开 | 用户ID: $userId")
            }
        }
    }

}