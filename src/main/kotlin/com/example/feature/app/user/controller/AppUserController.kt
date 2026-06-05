package com.example.feature.app.user.controller

import com.example.feature.app.user.service.AppUserService
import com.example.common.dto.ApiResult
import com.example.feature.KtorAppController
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.routing.*
import io.ktor.server.response.respond

class AppUserController(
    private val appUserService: AppUserService
): KtorAppController {

    override fun Route.registerRoutes() {
        route("/user") {
            rateLimit(RateLimitName("per_ip_limit")) {
                get("/{id}") {
                    val id = call.parameters["id"]!!.toLong()


                    call.respond(
                        ApiResult.ok<Unit>()
                    )
                }
            }



        }
    }

}