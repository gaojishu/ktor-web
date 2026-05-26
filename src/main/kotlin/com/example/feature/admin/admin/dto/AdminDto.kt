package com.example.feature.admin.admin.dto

import com.example.common.dto.BaseDto
import com.example.feature.admin.admin.enums.AdminStatusEnum
import com.example.feature.admin.permission.dto.PermissionDto
import kotlinx.serialization.Serializable

@Serializable
data class AdminDto(
    /** 用户名 */
    var username: String = "",

    /** 昵称 */
    var nickname: String? = null,

    /** 邮箱 */
    var email: String? = null,

    /** 手机号 */
    var mobile: String? = null,

    /**
     * 权限标识
     * 直接透传 List，前端拿到的是标准的 JSON 数组格式 ["admin:add", "user:list"]
     */
    var permissionKey: List<String>? = null,

    /** 禁用状态枚举值 */
    var status: AdminStatusEnum? = null,

    var deletedAt: String? = null,

    var permission: List<PermissionDto> = emptyList()
): BaseDto()