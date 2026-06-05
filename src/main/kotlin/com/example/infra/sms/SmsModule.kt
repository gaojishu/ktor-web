package com.example.infra.sms

import com.example.infra.sms.config.AliyunSmsConfig
import com.example.infra.sms.enums.SmsProviderEnum
import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class SmsModule {

    @Single
    fun provideAliyunSmsConfig(config: ApplicationConfig): AliyunSmsConfig {
        return AliyunSmsConfig.fromConfig(config)
    }

    @Single
    fun provideDefaultSmsProvider(config: ApplicationConfig): SmsProviderEnum {
        val defaultStr = config.propertyOrNull("sms.default")?.getString()?.uppercase()
        return if (defaultStr != null) {
            SmsProviderEnum.valueOf(defaultStr)
        } else {
            SmsProviderEnum.ALIYUN
        }
    }
   
}