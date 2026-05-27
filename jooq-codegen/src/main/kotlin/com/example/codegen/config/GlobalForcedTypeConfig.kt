package com.example.codegen.config

import org.jooq.meta.jaxb.ForcedType

object GlobalForcedTypeConfig {
    val forcedTypes = listOf(
        ForcedType().apply {
            userType = "kotlin.uuid.Uuid"
            converter = "com.example.common.converter.GlobalKotlinUuidConverter"
            includeTypes = "UUID|uuid"
        },

    )
}