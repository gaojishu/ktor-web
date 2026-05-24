package com.example.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<T>(
    var code: Int = 0,
    var message: String? = null,
    var data:  T? = null,
    var success: Boolean = false
)