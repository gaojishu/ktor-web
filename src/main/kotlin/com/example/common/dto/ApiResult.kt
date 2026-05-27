package com.example.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<T>(
    var code: Int = 0,
    var message: String? = null,
    var data:  T? = null,
    var success: Boolean = false
){
    companion object {
        // 成功状态工厂函数
        fun <T> ok(data: T? = null, message: String?  = null): ApiResult<T> {
            return ApiResult(
                code = 0,
                message = message,
                data = data,
                success = true
            )
        }

        // 失败状态工厂函数
        fun <T> error(message: String? = "未知错误", code: Int = 0): ApiResult<T> {
            return ApiResult(
                code = code,
                message = message,
                data = null,
                success = false
            )
        }

    }
}