package com.example.plugins

import com.example.api.admin.adminRoutes
import com.example.api.app.appRoutes
import io.ktor.server.application.Application
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {

    routing {

        route("/api/app") {
            rateLimit(RateLimitName("per_ip_limit")) {
                appRoutes()
            }

        }

        route("/api/admin") {
            adminRoutes()
        }
    }
}