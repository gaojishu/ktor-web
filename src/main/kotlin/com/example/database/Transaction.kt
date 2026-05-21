package com.example.database

import org.jooq.DSLContext
import org.jooq.impl.DSL

inline fun <T> DSLContext.tx(
    crossinline block: (DSLContext) -> T
): T {

    return this.transactionResult { config ->
        block(DSL.using(config))
    }
}