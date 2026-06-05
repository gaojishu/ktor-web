package com.example.feature.common.addr3.dto

import kotlinx.serialization.Serializable

@Serializable
data class Addr3Dto(
    var id: Long? = null,
    var pid: Long? = null,
    var deep: Short? = null,
    var name: String? = null,
    var extPath: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null,
)
