package com.example.feature.app.user.admin

import com.example.infra.dto.ApiResult
import io.ktor.server.routing.*
import io.ktor.server.response.respond

fun Route.userRoute(
    userService: UserService
) {

    route("/user") {

        // 查询用户
        get("/{id}") {
            val id = call.parameters["id"]!!.toLong()

            userService.getUser(id)
            call.respond<ApiResult<Unit>>(
                ApiResult()
            )
        }


    }
}