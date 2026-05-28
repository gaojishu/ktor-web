package com.example.feature.admin.oplog.dto

import com.example.common.dto.PageQuery
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class OpLogQueryParams(
    val id: Uuid? = null,
    val method: String? = null,

    val createdAt: List<String> = listOf(),
    val updatedAt: List<String> = listOf(),
)


@Serializable
data class OpLogPageReq(
    var params: OpLogQueryParams? = null,
    var page: PageQuery = PageQuery()
)



