package com.example.infra.security

import io.ktor.server.config.*

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val expiresIn: Long,
    val audience: String,
    val realm: String,
    val excludePaths: List<String>
) {
    companion object {
        fun fromConfig(config: ApplicationConfig, path: String): JwtConfig {

            val subConfig = config.config(path)

            return JwtConfig(
                secret = subConfig.property("secret").getString(),
                issuer = subConfig.property("issuer").getString(),
                expiresIn = subConfig.property("expiresIn").getString().toLong(),
                audience = subConfig.property("audience").getString(),
                realm = subConfig.property("realm").getString(),

                excludePaths = subConfig.property("excludePaths").getList()
            )
        }
    }
}

