package com.example.plugins

import com.example.feature.app.user.app.AppUserValidation
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        AppUserValidation.register(this)
    }
}
