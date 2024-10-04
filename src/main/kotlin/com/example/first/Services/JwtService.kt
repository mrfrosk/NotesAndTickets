package com.example.first.Services

import com.example.first.configuration.yaml.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    @Autowired
    lateinit var properties: JwtProperties

    private fun generate(
        email: String,
        expirationDate: Date,
        secretKey: SecretKey,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()

    fun generateAccessToken(email: String): String {
        val duration = Duration.ofSeconds(properties.getAccessExpiration())
        val expirationTime = Date.from(Instant.now() + duration)
        return generate(email, expirationTime, properties.getAccessKey())
    }

    fun generateRefreshToken(email: String, instant: Instant?=null): String {
        val duration = Duration.ofSeconds(properties.getRefreshExpiration())
        val expirationTime = Date.from(instant ?: (Instant.now() + duration))
        return generate(email, expirationTime, properties.getRefreshKey())
    }

    fun getEmail(token: String): String {
        return getAllClaims(token).subject
    }

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(properties.getAccessKey())
            .build()

        return parser.parseSignedClaims(token).payload
    }
}