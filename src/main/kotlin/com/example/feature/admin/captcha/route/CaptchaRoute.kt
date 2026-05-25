package com.example.feature.admin.captcha.route

import com.example.common.dto.ApiResult
import com.example.feature.admin.KtorAdminController
import com.example.feature.admin.captcha.dto.CaptchaDto
import com.example.feature.admin.captcha.service.CaptchaService
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single

@Single
class CaptchaRoute(
    private val captchaService: CaptchaService
) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/captcha") {
            get("/create") {
                val res = captchaService.create()
                call.respond<ApiResult<CaptchaDto>>(
                    ApiResult(
                        data = res
                    )
                )
            }
        }
    }
}


