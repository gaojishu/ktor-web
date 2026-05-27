package com.example.feature.admin.permission.validation

import com.example.feature.KtorValidator
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import org.koin.core.annotation.Single

@Single
class PermissionValidation: KtorValidator {
    override fun register(config: RequestValidationConfig) {

    }
}