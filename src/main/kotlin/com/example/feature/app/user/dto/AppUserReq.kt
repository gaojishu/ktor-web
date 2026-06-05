package com.example.feature.app.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppUserReq(
    val username: String? = null,
    val password: String? = null
)