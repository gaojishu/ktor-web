package com.example.feature.admin.notice.dto

import com.example.common.dto.PageQuery
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class NoticeQueryParams(
    val id: Uuid? = null,
    val title: String? = null,

    val createdAt: List<String> = listOf(),
    val updatedAt: List<String> = listOf(),
)


@Serializable
data class NoticePageReq(
    var params: NoticeQueryParams? = null,
    var page: PageQuery = PageQuery()
)



