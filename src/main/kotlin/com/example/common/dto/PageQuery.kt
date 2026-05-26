package com.example.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageQuery(
    val page: Int = 1,
    val size: Int = 20,
) {
    val offset: Int
        get() = (page - 1) * size
}
