package com.example.feature.user.validation

import com.example.feature.user.dto.AppUserReq
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

object AppUserValidation {

    fun register(config: RequestValidationConfig) {

        config.validate<AppUserReq> { req ->
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