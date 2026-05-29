package com.example.feature.admin.oplog.controller

import com.example.common.dto.ApiResult
import com.example.feature.KtorAdminController
import com.example.feature.admin.oplog.dto.OpLogPageReq
import com.example.feature.admin.oplog.dto.OpLogQueryParams
import com.example.feature.admin.oplog.excel.OpLogExportService
import com.example.feature.admin.oplog.service.OpLogService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.core.annotation.Single

@Single
class OpLogController(
    private val opLogService: OpLogService,
    private val opLogExportService: OpLogExportService
): KtorAdminController {
    override fun Route.registerRoutes() {
        route("/oplog") {
            post("/page"){
                val req = call.receive<OpLogPageReq>()
                val res = opLogService.page(req)
                call.respond(ApiResult.ok(data = res))
            }

            post("/export"){
                val req = call.receive<OpLogQueryParams>()
                opLogExportService.startAsyncExport()
                call.respond(ApiResult.ok<Unit>())
            }
        }
    }
}