package com.example.feature.admin.admin.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AuthLoginReq (
    val username: String,
    val password: String,
    val captchaCode: String,
    @Contextual
    val captchaUuid: UUID,
)