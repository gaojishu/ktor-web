package com.example.feature.common.enums.controller

import com.example.common.dto.ApiResult
import com.example.feature.KtorCommonController
import com.example.feature.common.enums.service.EnumsService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.core.annotation.Single

@Single
class EnumsController(
    private val enumsService: EnumsService
) : KtorCommonController {

    override fun Route.registerRoutes() {
        route("/enums") {
            get("/list") {
                val res = enumsService.list()
                call.respond(
                    ApiResult(
                        data = res
                    )
                )
            }
        }
    }
}