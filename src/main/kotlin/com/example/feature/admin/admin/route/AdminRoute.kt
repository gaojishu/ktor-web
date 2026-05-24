package com.example.feature.admin.admin.route

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.admin.admin.dto.db.AdminDto
import com.example.feature.admin.admin.service.AdminService
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import java.util.UUID

fun Route.adminRoute(){
    val adminService by inject<AdminService>()

    route("/admin") {
        get("/{id}") {
            val id = call.uuidId

            val a = adminService.detail(id)
            call.respond<ApiResult<AdminDto>>(
                ApiResult(
                    data = a
                )
            )
        }
    }
}

