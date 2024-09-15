package com.example.first.Controllers

import com.example.first.Services.AuthService
import com.example.first.Services.JwtService
import com.example.first.Services.dto.AuthDto
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode


@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    lateinit var authService: AuthService
    @Autowired
    lateinit var jwtService: JwtService

    @PostMapping("/login")
    fun authUser(@RequestBody authBody: String): ResponseEntity<*> {
        val userInfo = Json.decodeFromString<AuthDto>(authBody)
        val isAuth = authService.authUser(userInfo)
        val responseHeader = HttpHeaders()
        val token = jwtService.generateAccessToken(userInfo.email)
        if (isAuth){
            responseHeader.add("Bearer ", token)
        }
        return ResponseEntity.ok().headers(responseHeader).body(null)
    }
}