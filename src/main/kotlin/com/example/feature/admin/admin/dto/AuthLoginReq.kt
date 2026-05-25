package com.example.feature.admin.admin.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginReq (
    val username: String,
    val password: String,
    val captchaCode: String,
    val captchaUuid: String,
)