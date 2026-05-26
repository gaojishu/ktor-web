package com.example.common.functions

import com.password4j.Argon2Function
import com.password4j.Password
import com.password4j.types.Argon2

fun String.hashWithArgon2(): String {
    val argon2 = Argon2Function.getInstance(
        65536,   // memory (KB)
        3,       // iterations
        4,       // parallelism
        32,      // output length（32 bytes 常用）
        Argon2.ID
    )

    return Password.hash(this)
        .with(argon2)
        .result
}

/**
 * 验证当前的明文密码是否与传入的哈希值匹配
 * 使用方法: "myPassword123".verifyArgon2(hashedString)
 */
fun String.verifyArgon2(hashed: String): Boolean {
    if (hashed.isBlank()) return false
    return try {
        Password.check(this, hashed).withArgon2()
    } catch (_: IllegalArgumentException) {
        false
    }
}
