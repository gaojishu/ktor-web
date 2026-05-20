package com.example.api.app

import com.example.plugins.BusinessException
import com.example.tool.dto.ApiResult
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.appRoutes() {

    route("/user") {

        get("/me") {

            call.respond<ApiResult<Unit>>(
                ApiResult()
            )
        }
    }
}