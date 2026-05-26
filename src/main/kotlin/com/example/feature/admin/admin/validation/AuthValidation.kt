package com.example.feature.admin.admin.validation

import com.example.common.functions.shouldBeAlphanumeric
import com.example.common.functions.shouldBeStrongPassword
import com.example.common.functions.shouldNotBlank
import com.example.feature.KtorValidator
import com.example.feature.admin.admin.dto.AuthLoginReq
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import org.koin.core.annotation.Single

@Single
class AuthValidation : KtorValidator {

    override fun register(config: RequestValidationConfig) {
        config.validate<AuthLoginReq> { req ->
            val errors = mutableListOf<String>()

            req.username.shouldBeAlphanumeric("用户名")?.let { errorMsg ->
                errors.add(errorMsg)
            }

            req.password.shouldBeStrongPassword("密码")?.let { errorMsg ->
                errors.add(errorMsg)
            }

            req.captchaCode.shouldNotBlank("验证码")?.let { errorMsg ->
                errors.add(errorMsg)
            }


            if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
        }
    }
}
