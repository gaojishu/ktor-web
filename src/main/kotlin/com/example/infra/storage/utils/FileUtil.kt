package com.example.infra.storage.utils

import java.io.File
import java.net.URLConnection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.uuid.Uuid

object FileUtil {

    /**
     * 重命名文件
     */
    fun rename(objectName: String, visibility: String): String {
        val extension = File(objectName).extension

        val type = getType(objectName)

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val uuid = Uuid.generateV4().toString()

        return "${visibility}/$type/$currentDate/$uuid.$extension"
    }

    /**
     * 根据文件名获取mimeType
     */
    fun getType(objectName: String): String {
        val mimeType = URLConnection.guessContentTypeFromName(objectName) ?: "application/octet-stream"

        return mimeType.substringBefore("/")
    }
}