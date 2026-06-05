package com.example.feature.admin.notice.dto

import com.example.common.dto.BaseDto
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class NoticeDto(
    var adminId: Uuid? = null,
    var title: String? = null,
    var content: String? = null,
    var attachments: List<String> = emptyList(),
): BaseDto()



@Serializable
data class NoticeQueryParams(
    val id: Uuid? = null,
    val title: String? = null,

    val createdAt: List<String> = listOf(),
    val updatedAt: List<String> = listOf(),
)


