package com.example.feature.admin.permission.dto

import com.example.common.dto.BaseDto
import com.example.feature.admin.permission.enums.PermissionTypeEnum
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PermissionDto(
    var icon: String? = null,
    var level: Int? = null,
    var name: String? = null,
    @Contextual
    var parentId: UUID? = null,
    var path: String? = null,
    var remark: String? = null,
    var sort: Int? = null,
    var type: PermissionTypeEnum? = null,
    var code: String? = null,
    var key: String? = null,
): BaseDto()
