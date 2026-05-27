package com.example.feature.admin.captcha.controller

import com.example.common.dto.ApiResult
import com.example.feature.KtorAdminController
import com.example.feature.admin.captcha.dto.CaptchaDto
import com.example.feature.admin.captcha.service.CaptchaService
import io.ktor.server.routing.*
import io.ktor.server.response.respond
import org.koin.core.annotation.Single

@Single
class CaptchaController(
    private val captchaService: CaptchaService
) : KtorAdminController {

    override fun Route.registerRoutes() {
        route("/captcha") {
            get("/create") {
                val res = captchaService.create()
                call.respond<ApiResult<CaptchaDto>>(
                    ApiResult.ok(
                        data = res
                    )
                )
            }
        }
    }
}


