package com.example.infra.security

import io.ktor.server.config.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.annotation.Named

@Module
class SecurityModule {

    @Single(createdAtStart = true)
    @Named("adminJwtConfig")
    fun provideAdminJwt(config: ApplicationConfig): JwtConfig {
        return JwtConfig.fromConfig(config, "jwt.admin")
    }

    @Single(createdAtStart = true)
    @Named("appJwtConfig")
    fun provideAppJwt(config: ApplicationConfig): JwtConfig {
        return JwtConfig.fromConfig(config, "jwt.app")
    }

    @Single
    @Named("adminJwtService")
    fun provideAdminJwtService(@Named("adminJwtConfig") config: JwtConfig): JwtService {
        return JwtService(config)
    }

    @Single
    @Named("appJwtService")
    fun provideAppJwtService(@Named("appJwtConfig") config: JwtConfig): JwtService {
        return JwtService(config)
    }
}
