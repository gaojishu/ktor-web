package com.example.feature.admin.notice.service

import com.example.common.dto.PageResult
import com.example.feature.admin.notice.dto.NoticeDto
import com.example.feature.admin.notice.dto.NoticePageReq
import com.example.feature.admin.notice.repo.NoticeRepo
import com.example.infra.storage.StorageFactory
import org.koin.core.annotation.Single

@Single
class NoticeService(
    private val noticeRepo: NoticeRepo,
    private val storageFactory: StorageFactory
) {
    suspend fun page(req: NoticePageReq): PageResult<NoticeDto> {
        val res = noticeRepo.searchPage(req.params, req.page)

        res.list.forEach { notice ->
            notice.attachments = notice.attachments.map { path ->
                storageFactory.getProvider().generatePresignedUrl(path)
            }
        }

        return res
    }
}