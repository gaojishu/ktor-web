package com.example.common

import com.example.common.exception.BusinessException
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import kotlin.uuid.Uuid

// 编写扩展属性
val ApplicationCall.uuidId: Uuid
    get() {
        val idStr = parameters["id"] ?: throw BadRequestException("Missing id parameter")
        return try {
            Uuid.parse(idStr)
        } catch (_: IllegalArgumentException) {
            throw BusinessException("Invalid kotlin.uuid.Uuid format for id: $idStr")
        }
    }
