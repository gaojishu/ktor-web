package com.example.infra.storage.enums

import com.example.common.dto.ValueLabel

enum class StorageVisibilityEnum(val value: String, val label: String) {
    PRIVATE("private", "私有"),
    PUBLIC("public", "公有");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value } ?: PRIVATE
        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                // 前端传 value 后端不能解析，所以用使用 name
                ValueLabel(it.name, it.label)
            }
        }
    }
}