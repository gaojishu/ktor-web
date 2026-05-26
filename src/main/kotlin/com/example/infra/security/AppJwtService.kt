package com.example.infra.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.Date

@Single
class AppJwtService(
    @Named("appJwtConfig") private val config: JwtConfig
) {
    private val algorithm = Algorithm.HMAC256(config.secret)

    fun generateToken(userId: String): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withClaim("userId", userId) // 载荷注入自定义字段
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn * 1000))
            .sign(algorithm)
    }

    fun makeJwtVerifier(): JWTVerifier {
        return JWT
            .require(algorithm)
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .build()
    }

    fun excludePaths(): List<String> {
        return config.excludePaths
    }
}
