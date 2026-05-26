package com.example.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class ValueLabel<T>(
    val value: T,
    val label: String
)
