package com.example.infra.storage

import java.io.File
import java.io.InputStream

interface StorageProvider {
    fun generateUploadCredential(fileName: String): Map<String, String>

    fun upload(file: File,visibility: String): String
    fun delete(path: String)
    fun exists(path: String): Boolean
    fun generatePresignedUrl(path: String): String
    fun getFullUrl(path: String): String
    fun move(from: String, to: String)
    fun copy(from: String, to: String)
    fun uploadPolicy(): Map<String, String>
    fun rename(objectName: String, visibility: String): String

    fun removeUrl(url: String): String

    fun uploadStream(inputStream: InputStream, originalFileName: String): String
}