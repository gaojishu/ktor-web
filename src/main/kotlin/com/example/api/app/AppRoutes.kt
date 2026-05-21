package com.example.api.app

import com.example.modules.auth.dto.AuthLoginReq
import com.example.tool.dto.ApiResult
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.appRoutes() {


    route("/user") {
        post("/me") {
            val req = call.receive<AuthLoginReq>()

            println(req)
            call.respond<ApiResult<Unit>>(
                ApiResult()
            )
        }

        get("/me") {

            call.respond<ApiResult<Unit>>(
                ApiResult()
            )
        }
    }
}