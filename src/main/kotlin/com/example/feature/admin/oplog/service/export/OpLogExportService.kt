package com.example.feature.admin.oplog.service.export

import com.example.common.utils.log.log
import com.example.feature.admin.oplog.dto.OpLogDto
import com.example.feature.admin.oplog.dto.OpLogQueryParams
import com.example.feature.admin.oplog.repo.OpLogRepo
import com.example.infra.storage.StorageFactory
import com.example.infra.storage.enums.StorageVisibilityEnum
import org.koin.core.annotation.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.atomic.AtomicLong
import kotlin.uuid.Uuid

@Single
class OpLogExportService(
    private val opLogRepo: OpLogRepo,
    private val opLogExcelStreamWriter: OpLogExcelStreamWriter,
    private val storageFactory: StorageFactory,
) {
    // 1. 定义 Service 内部专属的后台异步协程作用域
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    /**
     * 外部调用的异步主入口（普通函数，调用后瞬间返回 202）
     */
    fun startAsyncExport(params: OpLogQueryParams?, onSuccess: (Long, String) -> Unit) {
        serviceScope.launch {
            val uuid = Uuid.generateV4()
            val objectKey = "exports/oplog-${uuid}.xlsx"

            // 在 IO 线程池中创建临时文件
            val tempFile = File.createTempFile("oplog-${uuid}", ".xlsx")

            try {
                // 执行流式导出（调用下方的私有挂起函数）
                val totalRows = executeStreamExport(params, tempFile)

                // 此时 Excel 已在本地完整生成，调用组件上传该文件
                val key = storageFactory.getProvider().uploadStream(tempFile.inputStream(), objectKey, StorageVisibilityEnum.PRIVATE)

                log.info("startAsyncExport to ${tempFile.absolutePath} key=$key")

                onSuccess(totalRows, key)

            } catch (e: Exception) {
                log.error("startAsyncExport ", e)
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
    private suspend fun executeStreamExport(params: OpLogQueryParams?, tempFile: File): Long = withContext(Dispatchers.IO) {
        val databaseChannel = Channel<OpLogDto>(5000)
        val writerChannel = Channel<OpLogDto>(5000)
        val rowCounter = AtomicLong(0) // 线程安全的计数器

        coroutineScope {
            // 协程 1：从数据库读取数据到 databaseChannel
            launch {
                try {
                    opLogRepo.streamExportData(params, databaseChannel)
                } finally {
                    databaseChannel.close()
                }
            }

            // ✨ 协程 2：桥接两个 Channel 并在这个过程中统计行数
            launch {
                try {
                    for (item in databaseChannel) {
                        rowCounter.incrementAndGet() // 每流过一条数据，计数器 +1
                        writerChannel.send(item)
                    }
                } finally {
                    writerChannel.close()
                }
            }

            // 协程 3：Excel 写入组件消费 writerChannel
            tempFile.outputStream().use { fos ->
                opLogExcelStreamWriter.writeDataFromChannel(writerChannel, fos)
            }
        }

        return@withContext rowCounter.get()
    }
}
