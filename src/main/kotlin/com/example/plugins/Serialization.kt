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

                // 全局的上下文序列化器
                serializersModule = SerializersModule {
                    contextual(UUID::class, UUIDSerializer)
                }
            }
        )
    }
}