package com.example.feature

import com.example.feature.admin.oplog.plugin.AdminOpLogPlugin
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.getKoin

fun Application.installModule() {
    install(DoubleReceive)

    val subscribers = getKoin().getAll<KtorSubscribe>()
    subscribers.forEach { subscriber ->
        subscriber.registerSubscriptions(this)
    }

    routing {
        get("/") {
            call.respondText { "Hello Word!" }
        }
        route("/app") {

        }


        route("/admin") {
            val controllers = getKoin().getAll<KtorAdminController>()
            authenticate("admin") {
                install(AdminOpLogPlugin)

                controllers.forEach { controller ->
                    with(controller) {
                        registerRoutes()
                    }
                }
            }

        }

        route("/common") {
            val controllers = getKoin().getAll<KtorCommonController>()

            controllers.forEach { controller ->
                with(controller) {
                    registerRoutes()
                }
            }

        }
    }
}