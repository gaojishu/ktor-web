package com.example.feature.admin.admin.dto

import com.example.feature.admin.admin.enums.AdminStatusEnum
import com.example.common.dto.PageQuery
import kotlinx.serialization.Serializable

@Serializable
data class AdminQueryParams(
    val id: String? = null,
    val username: String? = null,
    val status: AdminStatusEnum? = null,

    val createdAt: List<String> = listOf(),
    val updatedAt: List<String> = listOf(),
)


@Serializable
data class AdminPageReq(
    var params: AdminQueryParams? = null,
    var page: PageQuery = PageQuery()
)



