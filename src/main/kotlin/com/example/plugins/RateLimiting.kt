package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimiting() {
    install(RateLimit) {
        register(RateLimitName("per_ip_limit")) {

            rateLimiter(limit = 100, refillPeriod = 60.seconds)

            requestKey { call ->
                call.request.origin.remoteHost
            }
        }
    }
}