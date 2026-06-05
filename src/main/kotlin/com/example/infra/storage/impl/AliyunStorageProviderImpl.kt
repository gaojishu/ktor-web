package com.example.infra.storage.impl

import com.aliyun.oss.HttpMethod
import com.aliyun.oss.OSSClientBuilder
import com.example.infra.storage.StorageProvider
import java.io.File
import java.io.InputStream
import java.util.*

class AliyunStorageProviderImpl(
    endpoint: String,
    accessKeyId: String,
    accessKeySecret: String,
    bucket: String,
    uploadPrefix: String,
) : StorageProvider {

    private val ossClient = OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret)

    override fun getFullUrl(path: String): String {
        TODO("Not yet implemented")
    }

    override fun copy(from: String, to: String) {
        TODO("Not yet implemented")
    }

    override fun delete(path: String) {
        TODO("Not yet implemented")
    }

    override fun exists(path: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun generateUploadCredential(fileName: String): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun generatePresignedUrl(path: String): String {
        TODO("Not yet implemented")
    }

    override fun move(from: String, to: String) {
        TODO("Not yet implemented")
    }

    override fun removeUrl(url: String): String {
        TODO("Not yet implemented")
    }

    override fun rename(objectName: String, visibility: String): String {
        TODO("Not yet implemented")
    }

    override fun upload(file: File, visibility: String): String {
        TODO("Not yet implemented")
    }

    override fun uploadPolicy(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun uploadStream(inputStream: InputStream, originalFileName: String): String {
        TODO("Not yet implemented")
    }
}
