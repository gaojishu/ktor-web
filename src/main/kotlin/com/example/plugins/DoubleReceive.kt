package com.example.plugins

import io.ktor.http.content.MultiPartData
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.DoubleReceive

fun Application.configureDoubleReceive() {
    install(DoubleReceive){
        exclude<MultiPartData>()
    }
}
