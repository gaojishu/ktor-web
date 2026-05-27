package com.example.feature

import io.ktor.server.routing.Route

interface KtorAppController {
    fun Route.registerRoutes()
}