package com.example.first.service_test

import com.example.first.Services.JwtService
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertNotEquals

@SpringBootTest
class JwtServiceTest {
    @Autowired
    lateinit var jwtService: JwtService

    @Test
    fun generateToken(){
        val email = "email"
        val accessToken = jwtService.generateAccessToken(email)
        val refreshToken = jwtService.generateRefreshToken(email)

        assertNotEquals(accessToken, refreshToken)
        assertEquals(jwtService.getEmail(accessToken), jwtService.getEmail(refreshToken))
    }
}