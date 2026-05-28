package com.example.common

import com.example.common.exception.BusinessException
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.util.toMap
import kotlin.uuid.Uuid
import kotlinx.serialization.json.*

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


suspend fun ApplicationCall.getRequestParams(): String {

    val queryParams = request.queryParameters.toMap()

    val bodyText = runCatching {
        receiveText()
    }.getOrDefault("")

    return buildJsonObject {

        if (queryParams.isNotEmpty()) {

            putJsonObject("query") {

                queryParams.forEach { (key, values) ->
                    put(key, values.joinToString(","))
                }
            }
        }

        if (bodyText.isNotBlank()) {

            runCatching {

                put(
                    "body",
                    Json.parseToJsonElement(bodyText)
                )

            }.onFailure {

                put("body", bodyText)
            }
        }

    }.toString()
}
