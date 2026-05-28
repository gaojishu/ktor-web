package com.example.feature.admin.notice.query

import com.example.feature.admin.notice.dto.NoticeQueryParams
import com.example.jooq.generate.tables.references.NOTICE
import org.jooq.Condition
import org.jooq.impl.DSL
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Single
class NoticeQuery {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun buildCondition(params: NoticeQueryParams? = null): Condition {

        var condition = DSL.noCondition()

        params?.id?.let {
            condition = condition.and(NOTICE.ID.contains(it))
        }

        params?.title?.let {
            condition = condition.and(NOTICE.TITLE.contains(it))
        }



        params?.createdAt.takeIf { it?.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(
                    NOTICE.CREATED_AT.between(
                        LocalDateTime.parse(startTime, formatter),
                        LocalDateTime.parse(endTime, formatter)
                    )
                )
            }
        }

        params?.updatedAt.takeIf { it?.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(
                    NOTICE.UPDATED_AT.between(
                        LocalDateTime.parse(startTime, formatter),
                        LocalDateTime.parse(endTime, formatter)
                    )
                )
            }
        }

        return condition
    }
}