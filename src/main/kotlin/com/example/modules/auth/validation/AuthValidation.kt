package com.example.modules.auth.validation

import com.example.modules.auth.dto.AuthLoginReq
import io.ktor.server.plugins.requestvalidation.*

object AuthValidation {

    fun register(config: RequestValidationConfig) {

        config.validate<AuthLoginReq> { req ->
            val errors = mutableListOf<String>()

            if (req.username.isNullOrBlank()) {
                errors.add("username required")
            }

            if (req.password.isNullOrBlank()) {
                errors.add("password required")
            }

            if (errors.isEmpty()) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(errors)
            }
        }

    }
}