package com.example.feature

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig

interface KtorValidator {
    fun register(config: RequestValidationConfig)
}
