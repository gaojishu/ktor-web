package com.example.feature

import io.ktor.server.application.Application

interface KtorSubscribe {
    fun registerSubscriptions(application: Application)
}