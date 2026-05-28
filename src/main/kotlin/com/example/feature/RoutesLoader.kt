package com.example.feature

import com.example.common.getRequestParams
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.origin
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.getKoin

fun Application.installRoutes() {
    install(DoubleReceive)

    routing {
        get("/") {
            call.respondText { "Hello Word!" }
        }
        route("/api/app") {

        }


        route("/api/admin") {


            val controllers = getKoin().getAll<KtorAdminController>()

            authenticate("admin") {

                intercept(ApplicationCallPipeline.Plugins) {
                    val path = call.request.path()
                    val method = call.request.httpMethod.value
                    val ip = call.request.origin.remoteHost
                    val params = call.getRequestParams()
                    println("""
            ===== admin request =====
            path=$path
            method=$method
            ip=$ip
            params=$params
        """.trimIndent())
                    proceed()

                    println("admin 路由结束")
                }

                controllers.forEach { controller ->
                    with(controller) {
                        registerRoutes()
                    }
                }
            }

        }

        route("/api/common") {
            val controllers = getKoin().getAll<KtorCommonController>()

            controllers.forEach { controller ->
                with(controller) {
                    registerRoutes()
                }
            }

        }
    }
}