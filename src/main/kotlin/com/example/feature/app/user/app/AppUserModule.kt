package com.example.feature.app.user.app

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject

fun Route.userModule() {

    userRoute()
}
