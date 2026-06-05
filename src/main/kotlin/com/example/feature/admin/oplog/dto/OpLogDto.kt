package com.example.feature.admin.oplog.dto

import com.example.common.dto.BaseDto
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class OpLogDto(
    var adminId: Uuid? = null,
    var ip: String? = null,
    var method: String? = null,
    var uri: String? = null,
    var params: String? = null,
    var duration: Long? = null,
    var remark: String? = null,
): BaseDto()



@Serializable
data class OpLogQueryParams(
    val id: Uuid? = null,
    val method: String? = null,

    val createdAt: List<String> = listOf(),
    val updatedAt: List<String> = listOf(),
)

