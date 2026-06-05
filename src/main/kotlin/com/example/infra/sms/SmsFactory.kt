package com.example.infra.sms

import com.example.infra.sms.provider.SmsProvider
import com.example.infra.sms.enums.SmsProviderEnum
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class SmsFactory(
    providers: List<SmsProvider>,
    private val defaultProvider: SmsProviderEnum,
) {

    private val providerMap: Map<SmsProviderEnum, SmsProvider> = providers.associateBy { it.provider }

    fun getProvider(provider: SmsProviderEnum? = null): SmsProvider {
        val target = provider ?: defaultProvider
        return providerMap[target] ?: throw IllegalArgumentException("短信服务商 ${target.label} 未初始化")
    }
}
