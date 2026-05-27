package com.example.feature.admin.permission.controller

import com.example.feature.KtorAdminController
import io.ktor.server.routing.*
import org.koin.core.annotation.Single

@Single
class PermissionController: KtorAdminController {
    override fun Route.registerRoutes() {
        route("/permission") {
            get("create"){

            }
        }
    }
}