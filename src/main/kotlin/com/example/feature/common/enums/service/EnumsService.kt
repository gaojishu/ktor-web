package com.example.feature.common.enums.service

import com.example.common.dto.ValueLabel
import com.example.feature.admin.admin.enums.AdminStatusEnum
import com.example.feature.admin.permission.enums.PermissionTypeEnum
import org.koin.core.annotation.Single

@Single
class EnumsService {
    fun list(): Map<String, List<ValueLabel<String>>> {
        val enum = mapOf(
            AdminStatusEnum::class.simpleName!!
                    to AdminStatusEnum.getAllValueLabel(),
            PermissionTypeEnum::class.simpleName!!
                    to PermissionTypeEnum.getAllValueLabel(),

            )

        return enum
    }
}