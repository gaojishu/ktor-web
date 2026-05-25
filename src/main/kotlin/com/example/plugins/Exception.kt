package com.example.plugins

import com.example.common.dto.ApiResult
import com.example.common.exception.BusinessException
import com.example.common.exception.UnauthorizedException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond


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

        exception<UnauthorizedException> { call, cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ApiResult<Unit>(
                    message = cause.message ?: "服务器繁忙，请稍后再试"
                )
            )
        }

        exception<RequestValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResult<Unit>(
                    message = cause.reasons.joinToString(",")
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