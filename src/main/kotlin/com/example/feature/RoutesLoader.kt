package com.example.feature

import com.example.feature.user.userModule
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.installRoutes() {

    routing {
        get("/") {
            call.respondText { "Hello Word!" }
        }

        route("/api/app") {
            userModule()
        }

        route("/api/admin") {

        }
    }

}