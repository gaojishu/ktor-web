package com.example.feature.admin.websocket.subscribe

import com.example.common.event.ExportSuccessEvent
import com.example.feature.KtorSubscribe
import com.example.feature.admin.websocket.dto.WsMessage
import com.example.infra.websocket.WebSocketSessionManager
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class ExportSuccessEventSubscribe: KtorSubscribe {
    override fun registerSubscriptions(application: Application) {
        application.monitor.subscribe(ExportSuccessEvent){ event ->
            application.launch(Dispatchers.IO) {
                WebSocketSessionManager.sendToUser(
                    event.userId.toString(),
                    Json.encodeToString(WsMessage(
                        type = "notice",
                        payload = event
                    ))
                )
            }
        }
    }
}