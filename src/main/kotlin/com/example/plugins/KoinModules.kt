package com.example.plugins

import com.example.di.allModules
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoinModules() {

    install(Koin) {
        slf4jLogger()

        modules(allModules(environment.config))
    }
}