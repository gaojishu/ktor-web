package com.example.infra.websocket

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

object WebSocketSessionManager {
    // 使用 ConcurrentHashMap 保证线程安全
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    // 用户上线，保存会话
    fun addSession(userId: String, session: WebSocketSession) {
        sessions[userId] = session
    }

    // 用户下线，移除会话
    fun removeSession(userId: String) {
        sessions.remove(userId)
    }

    // 根据 userId 获取指定用户的会话（用于点对点定向发送消息）
    fun getSession(userId: String): WebSocketSession? {
        return sessions[userId]
    }

    // 获取当前所有在线用户的 userId 列表
    fun getAllOnlineUsers(): Set<String> {
        return sessions.keys
    }

    suspend fun sendToUser(userId: String, message: String): Boolean {
        val session = sessions[userId] ?: return false // 用户不在线，直接返回 false
        return try {
            // 利用该用户的专属会话发送消息帧
            session.outgoing.send(Frame.Text(message))
            true
        } catch (e: Exception) {
            // 如果发送时报错（可能连接刚断开但还没触发 removeSession），进行清理
            removeSession(userId)
            throw e
        }
    }
}
