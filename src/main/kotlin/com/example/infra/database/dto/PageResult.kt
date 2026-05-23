package com.example.infra.database.dto

data class PageResult<T>(
    val list: List<T>,
    val total: Long,
    val page: Int,
    val size: Int,
) {
    val totalPage: Long
        get() = (total + size - 1) / size
}