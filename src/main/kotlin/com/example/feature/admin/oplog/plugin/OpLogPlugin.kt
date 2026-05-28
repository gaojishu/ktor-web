package com.example.feature.admin.oplog.plugin

import com.example.common.getRequestParams
import com.example.feature.admin.oplog.repo.OpLogRepo
import com.example.infra.security.CurrentUser
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.auth.principal
import io.ktor.server.plugins.origin
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.util.AttributeKey
import org.koin.ktor.ext.get

// 定义一个内部使用的强类型 Key，用于在不同生命周期之间传递数据
private class LogContext(
    val startTime: Long,
    val path: String,
    val method: String,
    val ip: String,
    val params: String?
)
private val LogContextKey = AttributeKey<LogContext>("AdminLogContext")

val AdminOpLogPlugin = createRouteScopedPlugin("AdminOpLogPlugin") {

    // 钩子 1：进入请求时触发（并列，不要嵌套）
    onCall { call ->
        val startTime = System.currentTimeMillis()
        val path = call.request.path()
        val method = call.request.httpMethod.value
        val ip = call.request.origin.remoteHost
        val params = call.getRequestParams()

        // 将抓取到的数据暂存到当前 call 的 attributes 中
        val context = LogContext(startTime, path, method, ip, params)
        call.attributes.put(LogContextKey, context)
    }

    // 钩子 2：业务执行完毕、准备发送响应时触发（并列，不要嵌套）
    onCallRespond { call, _ ->
        // 从 attributes 中取出暂存的数据
        val context = call.attributes.getOrNull(LogContextKey) ?: return@onCallRespond

        val durationMs = System.currentTimeMillis() - context.startTime
        val principal = call.principal<CurrentUser>()
        val opLogRepo = call.application.get<OpLogRepo>()

        // 写入日志
        opLogRepo.create(
            adminId = principal?.id,
            ip = context.ip,
            method = context.method,
            uri = context.path,
            params = context.params,
            duration = durationMs
        )
    }
}