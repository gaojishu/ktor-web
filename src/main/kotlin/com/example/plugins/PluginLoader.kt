package com.example.plugins

import com.example.infra.security.configureSecurity
import io.ktor.server.application.Application

fun Application.installPlugins() {

    configureForwardedHeader()
    configureLogging()
    configureSerialization()
    configureCors()
    configureException()
    configureRateLimiting()
    configureCompression()
    configureWebSockets()
    configureKoinModules()


    configureRequestValidation()
    configureSecurity()

}