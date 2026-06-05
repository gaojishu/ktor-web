package com.example.feature.admin.notice.subscribe

import com.example.common.event.ExportSuccessEvent
import com.example.feature.KtorSubscribe
import com.example.feature.admin.notice.repo.NoticeRepo
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class ExportSuccessEventSubscribe(
    private val noticeRepo: NoticeRepo
): KtorSubscribe {
    override fun registerSubscriptions(application: Application) {
        application.monitor.subscribe(ExportSuccessEvent){ event ->
            application.launch(Dispatchers.IO) {
                noticeRepo.create(
                    adminId = event.userId,
                    title = event.title,
                    content = event.content,
                    attachments = listOf(event.fileKey)
                )
            }
        }
    }
}