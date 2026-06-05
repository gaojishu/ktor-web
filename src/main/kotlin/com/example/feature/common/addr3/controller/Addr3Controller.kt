package com.example.feature.common.addr3.controller

import com.example.feature.KtorCommonController
import com.example.feature.common.addr3.service.Addr3Service
import com.example.common.dto.ApiResult
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.core.annotation.Single

@Single
class Addr3Controller(
    private val addr3Service: Addr3Service,
) : KtorCommonController {

    override fun Route.registerRoutes() {
        route("/addr3") {
            get("/list") {
                val res = addr3Service.list()
                call.respond(ApiResult.ok(data = res))
            }
        }
    
    }
}
