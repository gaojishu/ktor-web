package com.example.feature.app.user.admin

import com.example.feature.app.user.UserRepo
import io.ktor.server.routing.Route
import org.koin.dsl.module
import org.koin.ktor.ext.inject

// 💡 正确做法：直接声明为一个 List 变量
val userKoinModuleList = listOf(
    module {
        single<UserRepo> { UserRepo(get()) }
        single<UserService> { UserService(get()) }
    }
)


fun Route.userModule() {
    val userService by inject<UserService>()
    userRoute(userService)
}
