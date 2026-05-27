package com.example.feature.admin.admin.query

import com.example.feature.admin.admin.dto.AdminQueryParams
import com.example.jooq.generate.tables.references.ADMIN
import org.jooq.Condition
import org.jooq.impl.DSL
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Single
class AdminQuery {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun buildCondition(params: AdminQueryParams? = null): Condition {

        var condition = DSL.noCondition()

        params?.id?.let {
            condition = condition.and(ADMIN.ID.contains(it))
        }

        params?.username?.let {
            condition = condition.and(ADMIN.USERNAME.contains(it))
        }

        params?.status?.let {
            condition = condition.and(ADMIN.STATUS.eq(it))
        }

        params?.createdAt.takeIf { it?.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(
                    ADMIN.CREATED_AT.between(
                    LocalDateTime.parse(startTime, formatter),
                    LocalDateTime.parse(endTime, formatter)
                ))
            }
        }

        params?.updatedAt.takeIf { it?.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(ADMIN.UPDATED_AT.between(
                    LocalDateTime.parse(startTime, formatter),
                    LocalDateTime.parse(endTime, formatter)
                ))
            }
        }

        return condition
    }

}