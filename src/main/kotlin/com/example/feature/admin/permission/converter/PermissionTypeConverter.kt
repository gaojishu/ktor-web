package com.example.feature.admin.permission.converter

import com.example.feature.admin.permission.enums.PermissionTypeEnum
import org.jooq.impl.AbstractConverter

class PermissionTypeConverter: AbstractConverter<Int, PermissionTypeEnum>(
    Int::class.javaObjectType,
    PermissionTypeEnum::class.java
) {
    override fun from(db: Int?): PermissionTypeEnum? = db?.let { PermissionTypeEnum.fromValue(it) }
    override fun to(enum: PermissionTypeEnum?): Int? = enum?.value
}