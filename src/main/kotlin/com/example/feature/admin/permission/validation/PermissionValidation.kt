package com.example.feature.admin.permission.validation

import com.example.common.functions.shouldNotBlank
import com.example.feature.KtorValidator
import com.example.feature.admin.permission.dto.PermissionDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import org.koin.core.annotation.Single

@Single
class PermissionValidation: KtorValidator {
    override fun register(config: RequestValidationConfig) {
        config.validate<PermissionDto> { req ->
            val errors = mutableListOf<String>()

            req.name.shouldNotBlank("权限名称")?.let { errorMsg ->
                errors.add(errorMsg)
            }

            if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
        }
    }
}