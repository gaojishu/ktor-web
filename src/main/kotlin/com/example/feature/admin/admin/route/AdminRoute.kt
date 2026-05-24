package com.example.feature.admin.admin.route

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.admin.KtorAdminController
import com.example.feature.admin.admin.dto.db.AdminDto
import com.example.feature.admin.admin.service.AdminService
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single

@Single
class AdminRoute(private val adminService: AdminService) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/admin") {
            get("/{id}") {
                val id = call.uuidId
                val users = adminService.detail(id)
                call.respond<ApiResult<AdminDto>>(
                    ApiResult(
                        data = users
                    )
                )
            }
            get("/aa") {

                call.respond<ApiResult<String>>(
                    ApiResult(
                        data = "aa"
                    )
                )
            }
        }
    }
}


