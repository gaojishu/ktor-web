package com.example.infra.database.dto

data class PageQuery(
    val page: Int = 1,
    val size: Int = 20,
) {
    val offset: Int
        get() = (page - 1) * size
}
