package com.example.feature.app.user.app

import kotlinx.serialization.Serializable

@Serializable
data class AppUserReq(
    val username: String? = null,
    val password: String? = null
)
