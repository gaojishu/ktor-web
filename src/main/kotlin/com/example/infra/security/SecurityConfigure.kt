package com.example.infra.security

import com.example.plugins.UnauthorizedException
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
