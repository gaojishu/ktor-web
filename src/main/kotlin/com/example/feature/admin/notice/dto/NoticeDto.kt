package com.example.feature.admin.notice.dto

import com.example.common.dto.BaseDto
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class NoticeDto(
    var adminId: Uuid? = null,
    var title: String? = null,
    var content: String? = null,
    var attachments: List<String>
): BaseDto()
