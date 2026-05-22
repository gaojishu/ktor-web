package com.example.infra.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<T>(
    val code: Int = 0,
    val message: String? = null,
    val data:  T? = null,
    val success: Boolean = false
)