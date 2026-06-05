package com.example.feature.admin.notice.controller

import com.example.common.dto.ApiResult
import com.example.feature.KtorAdminController
import com.example.feature.admin.notice.dto.NoticePageReq
import com.example.feature.admin.notice.service.NoticeService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.core.annotation.Single

@Single
class NoticeController(
    private val noticeService: NoticeService,
): KtorAdminController {
    override fun Route.registerRoutes() {
        route("/notice") {
            post("/page"){
                val req = call.receive<NoticePageReq>()
                val res = noticeService.page(req)
                call.respond(ApiResult.ok(data = res))
            }
        }
    }
}