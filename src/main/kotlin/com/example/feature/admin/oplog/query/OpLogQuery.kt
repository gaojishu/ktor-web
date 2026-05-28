package com.example.feature.admin.oplog.query

import com.example.feature.admin.oplog.dto.OpLogQueryParams
import com.example.jooq.generate.tables.references.OP_LOG
import org.jooq.Condition
import org.jooq.impl.DSL
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Single
class OpLogQuery {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun buildCondition(params: OpLogQueryParams? = null): Condition {

        var condition = DSL.noCondition()

        params?.id?.let {
            condition = condition.and(OP_LOG.ID.contains(it))
        }

        params?.method?.let {
            condition = condition.and(OP_LOG.METHOD.contains(it))
        }



        params?.createdAt.takeIf { it?.size == 2 }?.let { range ->
            val startTime = range[0]
            val endTime = range[1]
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                condition = condition.and(
                    OP_LOG.CREATED_AT.between(
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
                    OP_LOG.UPDATED_AT.between(
                        LocalDateTime.parse(startTime, formatter),
                        LocalDateTime.parse(endTime, formatter)
                    )
                )
            }
        }

        return condition
    }
}