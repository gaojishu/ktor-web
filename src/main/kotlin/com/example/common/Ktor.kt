package com.example.common

import com.example.plugins.BusinessException
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import java.util.UUID

// 编写扩展属性
val ApplicationCall.uuidId: UUID
    get() {
        val idStr = parameters["id"] ?: throw BadRequestException("Missing id parameter")
        return try {
            UUID.fromString(idStr)
        } catch (_: IllegalArgumentException) {
            throw BusinessException("Invalid UUID format for id: $idStr")
        }
    }
