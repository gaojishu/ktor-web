package com.example.feature.admin.role.dto

import com.example.common.dto.BaseDto
import kotlinx.serialization.Serializable

@Serializable
data class RoleDto(
    var name: String? = null,
    var remark: String? = null,
    var permissionKey: List<String>? = null,
): BaseDto()