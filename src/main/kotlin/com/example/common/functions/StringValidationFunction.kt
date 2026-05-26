package com.example.common.functions

fun String?.shouldNotBlank(field: String? = null): String? {
    return if (this.isNullOrBlank()) "$field 不能为空" else null
}

// 🛠️ String 的扩展函数：如果通过返回 null，失败返回错误描述
fun String?.shouldBeAlphanumeric(field: String? = null): String? {
    if (this.isNullOrBlank()) return "$field 不能为空"

    val regex = """^[a-zA-Z0-9]{6,16}$""".toRegex()

    return if (!this.matches(regex)) {
        "$field 格式不正确：需 6-16 位字母或数字"
    } else {
        null
    }
}

/**
 * 验证强密码：需 8-16 位且包含字母、数字及特殊符号
 */
fun String?.shouldBeStrongPassword(field: String? = "密码"): String? {
    if (this.isNullOrBlank()) return "$field 不能为空"

    val passwordRegex = """^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&._-])[A-Za-z\d@$!%*?&._-]{8,16}$""".toRegex()

    return if (!this.matches(passwordRegex)) {
        "$field 强度不足：需8-16位且包含字母、数字及特殊符号@$!%*?&._-"
    } else {
        null
    }
}

fun String?.shouldMinLength(field: String? = null, min: Int): String? {
    if (this == null) return null
    return if (this.length < min) "$field 长度不能少于 $min 位" else null
}

