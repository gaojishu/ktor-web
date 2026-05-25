package com.example.infra.security

import com.example.common.exception.UnauthorizedException
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.path
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val adminJwtService by inject<JwtService>(named("adminJwtService"))

    val appJwtService by inject<JwtService>(named("appJwtService"))

    install(Authentication) {
        jwt("admin") {
            authHeader { call ->
                // 优先从 Header 拿，拿不到就去 URL 参数里找一个叫 "token" 的值
                val header = call.request.parseAuthorizationHeader()
                if (header != null) return@authHeader header

                val queryToken = call.request.queryParameters["token"]
                if (queryToken != null) {
                    return@authHeader HttpAuthHeader.Single("Bearer", queryToken)
                }
                null
            }

            verifier(adminJwtService.makeJwtVerifier())

            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                if(userId != null){
                    JWTPrincipal(credential.payload)
                }else{
                    null
                }
            }

            skipWhen { call ->
                val requestPath = call.request.path()
                val excludePaths = adminJwtService.excludePaths()

                excludePaths.any { excludePath ->
                    if (excludePath.endsWith("/**")) {
                        requestPath.startsWith(excludePath.removeSuffix("/**"))
                    } else {
                        requestPath == excludePath
                    }
                }
            }

            challenge { _, _ ->
                throw UnauthorizedException("Token 校验失败或已过期")
            }
        }

        jwt("app") {

            authHeader { call ->
                // 优先从 Header 拿，拿不到就去 URL 参数里找一个叫 "token" 的值
                val header = call.request.parseAuthorizationHeader()
                if (header != null) return@authHeader header

                val queryToken = call.request.queryParameters["token"]
                if (queryToken != null) {
                    return@authHeader HttpAuthHeader.Single("Bearer", queryToken)
                }
                null
            }

            verifier(appJwtService.makeJwtVerifier())

            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                if(userId != null){
                    JWTPrincipal(credential.payload)
                }else{
                    null
                }
            }

            skipWhen { call ->
                val requestPath = call.request.path()
                val excludePaths = appJwtService.excludePaths()

                excludePaths.any { excludePath ->
                    if (excludePath.endsWith("/**")) {
                        requestPath.startsWith(excludePath.removeSuffix("/**"))
                    } else {
                        requestPath == excludePath
                    }
                }
            }

            challenge { _, _ ->
                throw UnauthorizedException("Token 校验失败或已过期")
            }
        }
    }
}
