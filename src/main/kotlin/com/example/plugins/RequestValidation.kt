package com.example.plugins

import com.example.feature.KtorValidator
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import org.koin.ktor.ext.getKoin

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        val validators = getKoin().getAll<KtorValidator>()

        // 遍历并自动注册
        validators.forEach { validator ->
            validator.register(this)
        }
    }
}
