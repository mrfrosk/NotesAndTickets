package com.example.first.Services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtService {

    @Autowired
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Autowired
    @Value("\${jwt.access-expired}")
    private lateinit var accessExpired: String

    @Autowired
    @Value("\${jwt.refresh-expired}")
    private lateinit var refreshExpired: String

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
        val duration = Duration.ofSeconds(accessExpired.toLong())
        val expirationTime = Date.from(Instant.now() + duration)
        return generate(email, expirationTime)
    }

    fun generateRefreshToken(email: String): String {
        val duration = Duration.ofSeconds(refreshExpired.toLong())
        val expirationTime = Date.from(Instant.now() + duration)
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