package com.example.first.Services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtService {
    private val secret: String = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855"
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    private fun generate(
        email: String,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(key)
            .compact()

    fun generateAccessToken(email: String): String {
        val expirationTime = Date.from(Instant.now() + Duration.ofMinutes(50))
        return generate(email, expirationTime)
    }

    fun generateRefreshToken(email: String): String {
        val expirationTime = Date.from(Instant.now() + Duration.ofDays(30))
        return generate(email, expirationTime)
    }

    fun getEmail(token: String): String {
        return getAllClaims(token).subject
    }

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(key)
            .build()

        return parser.parseSignedClaims(token).payload
    }
}