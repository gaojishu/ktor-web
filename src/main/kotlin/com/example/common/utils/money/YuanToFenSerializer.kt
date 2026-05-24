package com.example.common.utils.money

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal
import java.math.RoundingMode

/**
 *  @Serializable(with = YuanToFenSerializer::class)
 */
object YuanToFenSerializer : KSerializer<Long> {

    // 代理 Long 的基础序列化器描述符，既安全又不需要任何 @OptIn
    override val descriptor: SerialDescriptor = Long.serializer().descriptor

    // 前端传元（String），后端接收自动转成分（Long）
    override fun deserialize(decoder: Decoder): Long {
        val yuanStr = decoder.decodeString()
        if (yuanStr.isBlank()) return 0L
        return BigDecimal(yuanStr).multiply(BigDecimal("100")).toLong()
    }

    // 后端返回给前端时，自动把分（Long）转成元（String）输出
    override fun serialize(encoder: Encoder, value: Long) {
        val yuanStr = BigDecimal.valueOf(value)
            .divide(BigDecimal("100"), 2, RoundingMode.HALF_EVEN)
            .toPlainString()
        encoder.encodeString(value = yuanStr)
    }
}
