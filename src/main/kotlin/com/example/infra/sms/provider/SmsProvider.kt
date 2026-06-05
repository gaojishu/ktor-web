package com.example.infra.sms.provider

import com.example.infra.sms.enums.SmsProviderEnum

interface SmsProvider {
    val provider: SmsProviderEnum

    fun sendCode(phone: String, code: String, templateCode: String)
}