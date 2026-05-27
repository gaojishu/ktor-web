package com.example.feature.admin.admin.controller

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.KtorAdminController
import com.example.feature.admin.admin.dto.AdminPageReq
import com.example.feature.admin.admin.service.AdminService
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single

@Single
class AdminController(private val adminService: AdminService) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/admin") {
            post("/page") {
                val req = call.receive<AdminPageReq>()

                val res = adminService.page(req)
                call.respond(
                    ApiResult.ok(res)
                )
            }
            get("/{id}") {
                val id = call.uuidId
                val res = adminService.detail(id)
                call.respond(
                    ApiResult.ok(res)
                )
            }
        }
    }
}


