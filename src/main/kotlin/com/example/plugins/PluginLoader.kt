package com.example.plugins

import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureSerialization()
    configureException()
    configureLogging()
    configureRateLimiting()

    configureForwardedHeader()
    configureCompression()
    configureKoinModules()
    configureRequestValidation()
    configureWebSockets()
}