package com.example.modules

import com.example.modules.auth.validation.AuthValidation
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.installModules() {

    install(RequestValidation) {
        AuthValidation.register(this)
    }
}