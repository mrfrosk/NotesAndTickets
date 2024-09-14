package com.example.first.Controllers

import com.example.first.Services.AuthService
import com.example.first.Services.dto.AuthDto
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    lateinit var authService: AuthService

    @PostMapping("/login")
    fun authUser(@RequestBody authBody: String): Boolean {
        val userInfo = Json.decodeFromString<AuthDto>(authBody)
        return authService.authUser(userInfo)
    }
}