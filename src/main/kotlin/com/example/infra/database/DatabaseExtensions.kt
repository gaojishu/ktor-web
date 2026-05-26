package com.example.infra.database

import com.example.common.dto.PageQuery
import com.example.common.dto.PageResult
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SelectLimitStep
import org.jooq.impl.DSL
import java.time.LocalDateTime

inline fun <T> DSLContext.tx(
    crossinline block: (DSLContext) -> T
): T {

    return this.transactionResult { config ->
        block(DSL.using(config))
    }
}

fun <R : org.jooq.Record> SelectLimitStep<R>.page(
    dsl: DSLContext,
    query: PageQuery
): PageResult<R> {

    val total = dsl.fetchCount(this).toLong()

    val list = this
        .limit(query.size)
        .offset(query.offset)
        .fetch()

    return PageResult(
        list = list,
        total = total,
        page = query.page,
        size = query.size
    )
}

fun <T : Any> SelectLimitStep<*>.pageInto(
    dsl: DSLContext,
    query: PageQuery,
    clazz: Class<T>
): PageResult<T> {

    val total = dsl.fetchCount(this).toLong()

    val list = this
        .limit(query.size)
        .offset(query.offset)
        .fetchInto(clazz)

    return PageResult(list, total, query.page, query.size)
}


// 统一的软删除扩展函数
fun DSLContext.softDelete(table: org.jooq.Table<*>, whereCondition: org.jooq.Condition): Int {
    val deletedAtField = table.field("deleted_at", LocalDateTime::class.java)
        ?: throw IllegalArgumentException("表 ${table.name} 缺少 deleted_at 字段")

    return this.update(table)
        .set(deletedAtField, LocalDateTime.now())
        .where(whereCondition)
        .execute()
}

// 这里的 R 代表具体的 Record 类型（如 UsersRecord）
fun <R : Record> DSLContext.selectActiveFrom(table: org.jooq.Table<R>) =
    this.selectFrom(table).where(
        if (table.field("deleted_at") != null) {
            table.field("deleted_at")!!.isNull
        } else {
            DSL.noCondition()
        }
    )