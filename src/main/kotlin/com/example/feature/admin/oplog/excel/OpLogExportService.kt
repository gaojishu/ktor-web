package com.example.feature.admin.oplog.excel

import com.example.common.utils.log.log
import com.example.feature.admin.oplog.dto.OpLogDto
import com.example.feature.admin.oplog.repo.OpLogRepo
import org.koin.core.annotation.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.uuid.Uuid

@Single
class OpLogExportService(
    private val opLogRepo: OpLogRepo,
    private val opLogExcelStreamWriter: OpLogExcelStreamWriter,
) {
    // 1. 定义 Service 内部专属的后台异步协程作用域
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    /**
     * 外部调用的异步主入口（普通函数，调用后瞬间返回 202）
     */
    fun startAsyncExport() {
        // 2. 整个生命周期（包括文件创建、写入、上传、删除）全部移入后台协程
        serviceScope.launch {
            val uuid = Uuid.generateV4()
            val objectKey = "exports/oplog-${uuid}.xlsx"

            // 在 IO 线程池中创建临时文件
            val tempFile = File.createTempFile("oplog-${uuid}", ".xlsx")

            try {
                // 3. 执行流式导出（调用下方的私有挂起函数）
                executeStreamExport(tempFile)

                // 4. 此时 Excel 已在本地完整生成，调用 OSS 组件上传该文件
                //val ossUrl = ossUploader.uploadLocalFile(objectKey, tempFile)

                // 成功

                log.info("Successfully exported to ${tempFile.absolutePath}")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (tempFile.exists()) {
                    tempFile.delete()
                }
            }
        }
    }

    /**
     * 内部实际执行的流式写入逻辑（保持原样的挂起函数，但去掉了外层的删除逻辑）
     */
    private suspend fun executeStreamExport(tempFile: File) = withContext(Dispatchers.IO) {
        val dataChannel = Channel<OpLogDto>(5000)
        coroutineScope {
            // 启动异步生产者协程：从数据库读数据
            launch {
                try {
                    opLogRepo.streamExportData(dataChannel)
                } finally {
                    dataChannel.close()
                }
            }
            tempFile.outputStream().use { fos ->
                opLogExcelStreamWriter.writeDataFromChannel(dataChannel, fos)
            }
        }
    }
}
