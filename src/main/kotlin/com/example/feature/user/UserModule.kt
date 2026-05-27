package com.example.feature.user

import com.example.feature.user.controller.appUserRoute
import io.ktor.server.routing.Route

fun Route.userModule() {
    appUserRoute()
}
