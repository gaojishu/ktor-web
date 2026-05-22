package com.example.feature.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppUserReq(
    val username: String? = null,
    val password: String? = null
)