package com.example.feature.admin

import io.ktor.server.routing.Route

interface KtorAdminController {
    fun Route.registerRoutes()
}