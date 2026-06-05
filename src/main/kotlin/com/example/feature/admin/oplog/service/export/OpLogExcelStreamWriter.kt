package com.example.feature.admin.oplog.service.export

import com.example.feature.admin.oplog.dto.OpLogDto
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.koin.core.annotation.Single
import java.io.OutputStream

@Single
class OpLogExcelStreamWriter {

    suspend fun writeDataFromChannel(channel: ReceiveChannel<OpLogDto>, outputStream: OutputStream) {
        // SXSSF 保持 1000 行在内存，超出部分会自动写到 POI 自身的磁盘临时文件中
        SXSSFWorkbook(1000).use { workbook ->
            var sheet = workbook.createSheet("数据列表")
            createHeader(sheet)

            var rowIndex = 1
            var totalCount = 1

            for (dto in channel) {
                // 如果超过 100 万行，自动切 Sheet
                if (rowIndex >= 1000000) {
                    sheet = workbook.createSheet("数据列表_${totalCount / 1000000 + 1}")
                    createHeader(sheet)
                    rowIndex = 1
                }

                val row = sheet.createRow(rowIndex++)
                row.createCell(0).setCellValue(dto.id.toString())
                row.createCell(1).setCellValue(dto.method)
                row.createCell(2).setCellValue(dto.createdAt)

                if (totalCount++ % 10000 == 0) {
                    (sheet as SXSSFSheet).flushRows(1000)
                }
            }

            // 将完整的 Excel 写入我们指定的 tempFile 输出流中
            workbook.write(outputStream)

            // 显式清理 POI 在 /tmp 目录下产生的磁盘临时碎片
            workbook.close()
        }
    }

    private fun createHeader(sheet: Sheet) {
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("ID")
        headerRow.createCell(1).setCellValue("名称")
        headerRow.createCell(2).setCellValue("创建时间")
    }
}
