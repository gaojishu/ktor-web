package com.example.infra.sms.enums

import com.example.common.dto.ValueLabel

enum class SmsProviderEnum(val value: String, val label: String) {
    ALIYUN("aliyun", "阿里云短信");
   

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value } ?: ALIYUN

        fun getAllValueLabel(): List<ValueLabel<String>> {
            return entries.map {
                // 前端传 value 后端不能解析，所以用使用 name
                ValueLabel(it.name, it.label)
            }
        }
    }
}