package com.example.feature.admin.admin.route

import com.example.common.dto.ApiResult
import com.example.common.uuidId
import com.example.feature.admin.KtorAdminController
import com.example.feature.admin.admin.dto.AdminDto
import com.example.feature.admin.admin.dto.AuthLoginReq
import com.example.feature.admin.admin.service.AuthService
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Single
class AuthRoute(
    private val authService: AuthService
) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/auth") {
            post("/login") {
                val req = call.receive<AuthLoginReq>()

                val res = authService.login(req)
                call.respond<ApiResult<AdminDto>>(
                    ApiResult(
                        data = res
                    )
                )
            }
            get("/info") {
                val users = authService.info(Uuid.generateV7().toJavaUuid())
                call.respond<ApiResult<AdminDto>>(
                    ApiResult(
                        data = users
                    )
                )
            }
        }
    }
}


