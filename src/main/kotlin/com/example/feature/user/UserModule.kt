package com.example.feature.user

import com.example.feature.user.route.appUserRoute
import io.ktor.server.routing.Route

fun Route.userModule() {
    appUserRoute()
}
