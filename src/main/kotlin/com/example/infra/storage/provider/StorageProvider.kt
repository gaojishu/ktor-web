package com.example.infra.storage.provider

import com.example.infra.storage.enums.StorageProviderEnum
import com.example.infra.storage.enums.StorageVisibilityEnum
import java.io.File
import java.io.InputStream

interface StorageProvider {

    val provider: StorageProviderEnum

    fun upload(file: File, visibility: StorageVisibilityEnum): String
    fun delete(path: String)
    fun exists(path: String): Boolean
    fun generatePresignedUrl(path: String): String
    fun getFullUrl(path: String): String
    fun move(from: String, to: String)
    fun copy(from: String, to: String)
    fun uploadPolicy(): Map<String, String>

    fun removeUrl(url: String): String

    fun uploadStream(inputStream: InputStream, originalFileName: String, visibility: StorageVisibilityEnum): String
}