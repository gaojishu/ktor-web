package com.example.infra.database

import com.example.infra.database.dto.PageQuery
import com.example.infra.database.dto.PageResult
import org.jooq.DSLContext
import org.jooq.SelectLimitStep
import org.jooq.impl.DSL

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