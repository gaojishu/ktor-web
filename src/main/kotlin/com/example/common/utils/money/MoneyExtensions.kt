package com.example.common.utils.money

import java.math.BigDecimal
import java.math.RoundingMode

// ==================== 分 转 元 ====================

// 示例：100L.toYuan() -> "1.00"
fun Long.toYuanString(scale: Int = 2): String {
    return BigDecimal.valueOf(this)
        .divide(BigDecimal("100"), scale, RoundingMode.HALF_EVEN)
        .toPlainString()
}

// 示例：100L.toYuanBigDecimal() -> BigDecimal(1.00)
fun Long.toYuanBigDecimal(): BigDecimal {
    return BigDecimal.valueOf(this).divide(BigDecimal("100"), 2, RoundingMode.HALF_EVEN)
}

// ==================== 元 转 分 ====================

// 示例："1.23".toFen() -> 123L (最安全，推荐后端接收前端字符串时使用)
fun String.toFen(): Long {
    if (this.isBlank()) return 0L
    return BigDecimal(this).multiply(BigDecimal("100")).toLong()
}

// 示例：1.23.toFen() -> 123L
fun Double.toFen(): Long {
    return BigDecimal.valueOf(this).multiply(BigDecimal("100")).toLong()
}
