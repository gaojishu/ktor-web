package com.example.feature

import io.ktor.server.routing.Route

interface KtorAdminController {
    fun Route.registerRoutes()
}