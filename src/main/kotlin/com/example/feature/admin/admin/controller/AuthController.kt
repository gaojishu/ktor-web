package com.example.feature.admin.admin.controller

import com.example.common.dto.ApiResult
import com.example.feature.KtorAdminController
import com.example.feature.admin.admin.dto.AuthLoginReq
import com.example.feature.admin.admin.service.AuthService
import com.example.infra.security.CurrentUser
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single
import io.ktor.server.auth.*

@Single
class AuthController(
    private val authService: AuthService
) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/auth") {
            post("/login") {
                val req = call.receive<AuthLoginReq>()

                val res = authService.login(req)
                call.respond(
                    ApiResult.ok(res,"登录成功")
                )
            }
            get("/info") {
                val auth = call.authentication.principal<CurrentUser>()
                val res = authService.info(auth!!.id)
                call.respond(
                    ApiResult.ok(res)
                )
            }
            get("/permission"){
                val auth = call.authentication.principal<CurrentUser>()
                val res = authService.permission(auth!!.id)

                call.respond(
                    ApiResult.ok(res)
                )
            }
        }
    }
}


