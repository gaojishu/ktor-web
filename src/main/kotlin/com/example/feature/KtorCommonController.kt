package com.example.feature

import io.ktor.server.routing.Route

interface KtorCommonController {
    fun Route.registerRoutes()
}