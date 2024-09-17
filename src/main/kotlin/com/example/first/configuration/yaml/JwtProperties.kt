package com.example.first.configuration.yaml

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtProperties {
    @Autowired
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Autowired
    @Value("\${jwt.access.expiration}")
    private lateinit var accessExpiration: String

    @Autowired
    @Value("\${jwt.refresh.expiration}")
    private lateinit var refreshExpiration: String

    fun getKey(): SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())
    fun getAccessExpiration() = accessExpiration.toLong()
    fun getRefreshExpiration() = accessExpiration.toLong()

}