package com.example.first.Services

import com.example.first.configuration.yaml.JwtProperties
import com.example.first.database.dto.UserInfoDto
import com.example.first.database.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService() {

    @Autowired
    lateinit var jwtProperties: JwtProperties
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )
    fun generate(
        user: UserInfoDto,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(user.email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()
    fun isValid(token: String, user: User): Boolean {
        val tokenMail = extractEmail(token)
        val userMail = transaction { user.email }
        return userMail == tokenMail && !isExpired(token)
    }
    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject
    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))
    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()
        return parser
            .parseSignedClaims(token)
            .payload
    }
}