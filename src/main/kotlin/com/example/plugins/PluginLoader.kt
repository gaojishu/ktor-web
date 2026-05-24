package com.example.plugins

import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureForwardedHeader()
    configureLogging()
    configureSerialization()
    configureException()
    configureRateLimiting()
    configureCompression()
    configureRequestValidation()
    configureWebSockets()
    configureKoinModules()
}