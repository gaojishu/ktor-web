package com.example.api.admin

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.adminRoutes() {

    route("/user") {

        get {
            call.respondText("admin user list")
        }
    }
}