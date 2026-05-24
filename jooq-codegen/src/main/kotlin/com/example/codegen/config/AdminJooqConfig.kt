package com.example.codegen.config

import org.jooq.meta.jaxb.ForcedType

object AdminJooqConfig {
    val forcedTypes = listOf(
        // ---------admin start----------
        ForcedType().apply {
            userType = "com.example.feature.admin.admin.enums.AdminStatusEnum"
            converter = "com.example.feature.admin.admin.converter.AdminStatusConverter"
            includeExpression = "public\\.admin\\.status"
            includeTypes = "int4"
        },
        // ---------admin end----------
    )
}