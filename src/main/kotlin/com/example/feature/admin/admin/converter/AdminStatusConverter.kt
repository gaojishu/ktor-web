package com.example.feature.admin.admin.converter

import com.example.feature.admin.admin.enums.AdminStatusEnum
import org.jooq.impl.AbstractConverter

class AdminStatusConverter: AbstractConverter<Int, AdminStatusEnum>(
    Int::class.javaObjectType,
    AdminStatusEnum::class.java
) {
    override fun from(db: Int?): AdminStatusEnum? = db?.let { AdminStatusEnum.fromValue(it) }
    override fun to(enum: AdminStatusEnum?): Int? = enum?.value
}