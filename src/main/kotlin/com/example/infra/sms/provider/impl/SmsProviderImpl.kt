package com.example.infra.sms.provider.impl

import com.aliyun.dysmsapi20170525.Client
import com.aliyun.dysmsapi20170525.models.SendSmsRequest
import com.example.common.exception.BusinessException
import com.example.common.utils.log.log
import com.example.infra.sms.config.AliyunSmsConfig
import com.example.infra.sms.provider.SmsProvider
import org.koin.core.annotation.Single
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import com.example.infra.sms.enums.SmsProviderEnum

@Single
class AliyunSmsProviderImpl(
    private val config: AliyunSmsConfig,
    private val smsClient: Client,
) : SmsProvider {

    override val provider: SmsProviderEnum = SmsProviderEnum.ALIYUN

    override fun sendCode(phone: String, code: String, templateCode: String) {

        val templateParamJson = buildJsonObject {
            put("code", code)
        }.toString()

        val request = SendSmsRequest()
            .setPhoneNumbers(phone)
            .setSignName(config.signName)
            .setTemplateCode(templateCode)
            .setTemplateParam(templateParamJson)

        val response = smsClient.sendSms(request)
        log.info("smsCode: ${response.body.message}")
        if (response.body.code != "OK") {
            throw BusinessException("发送验证码错误：" + response.body.message)
        }
    }
}