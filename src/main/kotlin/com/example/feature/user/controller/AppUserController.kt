package com.example.feature.user.controller

import com.example.feature.user.service.AppUserService
import com.example.common.dto.ApiResult
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.appUserRoute(

) {
    val appUserService by inject<AppUserService>()

    route("/user") {
        rateLimit(RateLimitName("per_ip_limit")) {
            get("/{id}") {
                val id = call.parameters["id"]!!.toLong()

                appUserService.getUser(id)
                call.respond(
                    ApiResult.ok<Unit>()
                )
            }
        }



    }
}