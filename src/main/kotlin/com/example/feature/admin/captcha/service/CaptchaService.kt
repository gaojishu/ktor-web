package com.example.feature.admin.captcha.service

import cn.hutool.captcha.CaptchaUtil
import com.example.common.exception.BusinessException
import com.example.feature.admin.captcha.dto.CaptchaDto
import com.example.infra.redis.RedisRepository
import org.koin.core.annotation.Single
import java.util.UUID
import kotlin.uuid.toKotlinUuid

@Single
class CaptchaService(
    private val redisRepository: RedisRepository
) {
    private val redisPrefix = "captcha:"
    private val expireSeconds = 300L // 验证码 5 分钟有效

    suspend fun create(): CaptchaDto{
        val captcha = CaptchaUtil.createCircleCaptcha(140, 48, 4, 20)

        val code = captcha.code.lowercase()
        val uuid = UUID.randomUUID()

        redisRepository.setEx(redisPrefix + uuid,code,expireSeconds)

        return CaptchaDto(
            uuid = uuid,
            imageBase64Data = captcha.imageBase64Data,
        )
    }

    suspend fun verify(uuid: UUID, inputCode: String) {
        if (inputCode.isBlank()) {
            throw BusinessException("验证码错误")
        }

        val key = redisPrefix + uuid
        val cacheCode = redisRepository.get(key) ?: throw BusinessException("验证码错误")

        if (inputCode.trim().lowercase() != cacheCode) {
            throw BusinessException("验证码错误")
        }

        redisRepository.del(key)
    }
}