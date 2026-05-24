package com.example.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 🎯 跨平台/Native 友好的强类型 LocalDateTime 序列化适配器
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // 使用 String 序列化器的描述符，完美绕过 PrimitiveSerialDescriptor 的引入报错
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        // 编码时：将 LocalDateTime 转化为 String 吐给前端
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        // 解码时：接收前端传递的 String 并还原回 LocalDateTime
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}