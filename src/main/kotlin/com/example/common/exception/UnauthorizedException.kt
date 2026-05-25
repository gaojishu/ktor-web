package com.example.common.exception

class UnauthorizedException(message: String = "未经授权或 Token 已过期") : RuntimeException(message)
