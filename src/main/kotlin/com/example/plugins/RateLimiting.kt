package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimiting(){
    install(RateLimit) {
        register {
            rateLimiter(limit = 3, refillPeriod = 10.seconds) // 方便测试：10秒内最多3次
        }
    }
}