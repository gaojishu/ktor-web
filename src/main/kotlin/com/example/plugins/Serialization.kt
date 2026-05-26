package com.example.plugins

import com.example.common.serializer.UUIDSerializer
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDateTime
import java.util.UUID

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                // 强制序列化默认值，不要忽略它们！
                encodeDefaults = true

                // 明确保留 null 值，不要从 JSON 中移除
                explicitNulls = true

                // 核心配置：忽略前端多传的未知字段 🔴
                ignoreUnknownKeys = true

                // 可选配置：如果前端传了 null 但后端有默认值，自动使用默认值
                coerceInputValues = true

                // 全局的上下文序列化器
                serializersModule = SerializersModule {
                    contextual(UUID::class, UUIDSerializer)
                }
            }
        )
    }
}