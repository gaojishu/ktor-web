package com.example.feature.admin.permission.controller

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.KtorAdminController
import com.example.feature.admin.permission.dto.PermissionDto
import com.example.feature.admin.permission.service.PermissionService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.core.annotation.Single

@Single
class PermissionController(
    private val permissionService: PermissionService
): KtorAdminController {
    override fun Route.registerRoutes() {
        route("/permission") {
            post("/create") {
                val req = call.receive<PermissionDto>()
                permissionService.create(req)
                call.respond(ApiResult.ok<Unit>(message = "操作成功"))
            }
            post("/update") {
                val req = call.receive<PermissionDto>()
                permissionService.update(req)
                call.respond(ApiResult.ok<Unit>(message = "操作成功"))
            }
            get("/delete") {
                val id = call.uuidId
                permissionService.delete(id)
                call.respond(ApiResult.ok<Unit>(message = "操作成功"))
            }
            get("/list"){
                val res = permissionService.list()
                call.respond(ApiResult.ok(res))
            }
        }
    }
}