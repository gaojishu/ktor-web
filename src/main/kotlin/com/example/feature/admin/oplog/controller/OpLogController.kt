package com.example.feature.admin.oplog.controller

import com.example.common.dto.ApiResult
import com.example.common.event.ExportSuccessDto
import com.example.common.event.ExportSuccessEvent
import com.example.feature.KtorAdminController
import com.example.feature.admin.oplog.dto.OpLogExportReq
import com.example.feature.admin.oplog.dto.OpLogPageReq
import com.example.feature.admin.oplog.service.export.OpLogExportService
import com.example.feature.admin.oplog.service.OpLogService
import com.example.infra.security.CurrentUser
import io.ktor.server.auth.authentication
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
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
                val req = call.receive<OpLogExportReq>()
                val auth = call.authentication.principal<CurrentUser>()
                opLogExportService.startAsyncExport(req.params){ totalRows,key ->

                    application.monitor.raise(
                        ExportSuccessEvent,
                        ExportSuccessDto(
                            userId = auth!!.id,
                            title = "导出表格",
                            content = "导出日志表格成功，共导出 ${totalRows} 行数据",
                            fileKey = key,
                            totalRows = totalRows
                        )
                    )
                }

                call.respond(ApiResult.ok<Unit>(
                    message = "操作成功"
                ))
            }
        }
    }
}