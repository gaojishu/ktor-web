package com.example.plugins

import com.example.tool.dto.ApiResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

class BusinessException(message: String = "未知错误", val errorCode: Int = 0) : RuntimeException(message)

fun Application.configureException() {
    install(StatusPages) {

        // 捕获自定义的业务异常
        exception<BusinessException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResult<Unit>(
                    message = cause.message ?: "服务器繁忙，请稍后再试"
                )
            )
        }

        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResult<Unit>(
                    message = cause.message ?: "服务器繁忙，请稍后再试"
                )
            )
        }
    }
}