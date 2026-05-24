package com.example.common.utils.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 为任意非空类型扩展一个 log 属性
 * 自动以当前类的全限定名作为 Logger 的名称
 */
val <T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(this::class.java)
