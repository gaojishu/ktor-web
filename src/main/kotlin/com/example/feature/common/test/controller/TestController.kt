package com.example.feature.common.test.controller

import com.example.feature.KtorCommonController
import com.example.infra.storage.StorageFactory
import com.example.infra.storage.enums.StorageVisibilityEnum
import io.ktor.server.http.content.file
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.core.annotation.Single
import java.io.File

@Single
class TestController(
    private val storageFactory: StorageFactory
) : KtorCommonController {

    override fun Route.registerRoutes() {
        route("/test") {
            get("/file") {
//                val storage = storageFactory.getProvider()
//                    storage.getFullUrl("aa.jpg")
//
//                val testFile = File.createTempFile("test_upload", ".txt").apply {
//                    writeText("hello world - this is a test file content") // 写入测试内容
//                    deleteOnExit() // 🌟 关键：测试运行完自动删除，不留垃圾文件
//                }
//
//                val res = storage.uploadStream(testFile.inputStream(), "test_upload.txt",StorageVisibilityEnum.PUBLIC)
//                call.respond(res)
            }
        }
    }
}