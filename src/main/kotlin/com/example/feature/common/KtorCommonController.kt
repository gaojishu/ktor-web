package com.example.feature.common

import io.ktor.server.routing.Route

interface KtorCommonController {
    fun Route.registerRoutes()
}