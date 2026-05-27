package com.example.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

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
            }
        )
    }
}