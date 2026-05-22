package com.example.plugins

import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureSerialization()
    configureException()
    configureLogging()
    configureRateLimiting()
    configureRouting()
    configureForwardedHeader()
    configureCompression()
    configureKoinModules()
    configureRequestValidation()
    configureWebSockets()
}