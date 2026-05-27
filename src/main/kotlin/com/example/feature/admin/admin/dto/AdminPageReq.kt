package com.example.feature.admin.admin.dto

import com.example.feature.admin.admin.enums.AdminStatusEnum
import com.example.common.dto.PageQuery
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class AdminQueryParams(
    val id: Uuid? = null,
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



