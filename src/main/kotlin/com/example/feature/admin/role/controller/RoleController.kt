package com.example.feature.admin.role.controller

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.KtorAdminController
import com.example.feature.admin.role.dto.RoleDto
import com.example.feature.admin.role.service.RoleService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.core.annotation.Single

@Single
class RoleController(
    private val roleService: RoleService
): KtorAdminController {
    override fun Route.registerRoutes() {
        route("/role") {
            get("/list"){
                val res = roleService.list()
                call.respond(ApiResult.ok(data = res))
            }
            post("/create"){
                val req = call.receive<RoleDto>()
                val res = roleService.create(req)
                call.respond(ApiResult.ok(data = res, message = "操作成功"))
            }
            post("/update"){
                val req = call.receive<RoleDto>()
                val res = roleService.update(req)
                call.respond(ApiResult.ok(data = res, message = "操作成功"))
            }
            get("/delete"){
                val id = call.uuidId
                roleService.delete(id)
                call.respond(ApiResult.ok<Unit>(message = "操作成功"))
            }
        }
    }
}