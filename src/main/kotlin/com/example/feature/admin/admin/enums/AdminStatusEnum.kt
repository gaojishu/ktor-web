package com.example.feature.admin.admin.enums

import com.example.common.dto.ValueLabel

enum class AdminStatusEnum(val value: Int, val label: String) {
    PENDING(0, "待激活"),
    ACTIVE(1, "正常"),
    FREEZES(-1, "已禁用");

    companion object {
        fun fromValue(value: Int) = entries.firstOrNull { it.value == value } ?: PENDING

        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                // 前端传 value 识别不到，所以用使用 name
                ValueLabel(it.name, it.label)
            }
        }
    }
}
