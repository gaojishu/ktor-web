package com.example.common.exception

class BusinessException(message: String = "未知错误", val errorCode: Int = 0) : RuntimeException(message)
