package com.example.common.converter

import org.jooq.Converter
import java.util.UUID
import kotlin.uuid.Uuid

class GlobalKotlinUuidConverter : Converter<UUID, Uuid> {
    override fun from(databaseObject: UUID?): Uuid? {
        return databaseObject?.let {
            // 依赖 Kotlin 2.0.20+ 自带的工具或通过字节/字串互转
            Uuid.parse(it.toString())
        }
    }

    override fun to(userObject: Uuid?): UUID? {
        return userObject?.let { UUID.fromString(it.toString()) }
    }

    override fun fromType(): Class<UUID> = UUID::class.java
    override fun toType(): Class<Uuid> = Uuid::class.java
}
