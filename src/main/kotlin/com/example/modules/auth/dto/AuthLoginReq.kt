package com.example.modules.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginReq(
    val username: String? = null,
    val password: String? = null
)
