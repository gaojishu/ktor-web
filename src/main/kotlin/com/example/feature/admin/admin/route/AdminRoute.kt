package com.example.feature.admin.admin.route

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.admin.KtorAdminController
import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AdminPageReq
import com.example.feature.admin.admin.service.AdminService
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single

@Single
class AdminRoute(private val adminService: AdminService) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/admin") {
            post("/page") {
                val req = call.receive<AdminPageReq>()

                val res = adminService.page(req)
                call.respond(
                    ApiResult(
                        data = res
                    )
                )
            }
            get("/{id}") {
                val id = call.uuidId
                val res = adminService.detail(id)
                call.respond(
                    ApiResult(
                        data = res
                    )
                )
            }
        }
    }
}


