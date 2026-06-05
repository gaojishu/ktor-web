package com.example.feature.admin.websocket.dto

import kotlinx.serialization.Serializable

@Serializable
data class WsMessage<T>(
    val type: String,
    val payload: T
)

