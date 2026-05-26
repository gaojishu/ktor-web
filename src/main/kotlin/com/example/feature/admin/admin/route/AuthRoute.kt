package com.example.feature.admin.admin.route

import com.example.common.dto.ApiResult
import com.example.feature.admin.KtorAdminController
import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AuthLoginReq
import com.example.feature.admin.admin.dto.AuthLoginRes
import com.example.feature.admin.admin.service.AuthService
import com.example.infra.security.CurrentUser
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import io.ktor.server.auth.*

@Single
class AuthRoute(
    private val authService: AuthService
) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/auth") {
            post("/login") {
                val req = call.receive<AuthLoginReq>()

                val res = authService.login(req)
                call.respond(
                    ApiResult(
                        data = res
                    )
                )
            }
            get("/info") {
                val auth = call.authentication.principal<CurrentUser>()
                val res = authService.info(auth!!.id)
                call.respond(
                    ApiResult(
                        data = res
                    )
                )
            }
            get("/permission"){
                val auth = call.authentication.principal<CurrentUser>()
                val res = authService.permission(auth!!.id)

                call.respond(ApiResult(data = res))
            }
        }
    }
}


