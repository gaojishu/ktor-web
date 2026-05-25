package com.example.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    install(CORS) {
        // 1. 配置允许访问的前端域名（注意：不要加 http:// 或 https:// 前缀）
        allowHost("frontend.example.com", schemes = listOf("http", "https"))
        allowHost("localhost:3000")

        // 2. 配置允许的 HTTP 动词（默认只支持 GET, POST, HEAD）
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        // 3. 配置允许客户端携带的自定义请求头
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("X-Custom-Header")

        // 4. 允许客户端请求携带 Cookie 或 认证凭证
        allowCredentials = true

        // 5. 允许传输非标准或复杂的 Content-Type（例如 application/json）
        allowNonSimpleContentTypes = true
    }
}