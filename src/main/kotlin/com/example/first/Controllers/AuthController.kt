package com.example.first.Controllers

import com.example.first.Services.AuthService
import com.example.first.Services.JwtService
import com.example.first.Services.dto.AuthDto
import com.example.first.Services.dto.JwtDto
import com.example.first.Services.enums.LoginStatus
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders


@RestController
@RequestMapping(Mapping.AUTH)
class AuthController {
    @Autowired
    lateinit var authService: AuthService
    @Autowired
    lateinit var jwtService: JwtService

    @PostMapping("/login")
    suspend fun login(@RequestBody authBody: String): ResponseEntity<*> {
        val userInfo = Json.decodeFromString<AuthDto>(authBody)
        val responseHeader = HttpHeaders()
        val accessToken = jwtService.generateAccessToken(userInfo.email)
        val refreshToken = jwtService.generateRefreshToken(userInfo.email)
        val jwt = JwtDto(accessToken, refreshToken)
        val loginStatus = authService.login(userInfo)
        return if (loginStatus == LoginStatus.Success){
            ResponseEntity.ok().headers(responseHeader).body(jwt)
        }else{
            ResponseEntity.ok().headers(responseHeader).body(loginStatus.description)
        }
    }
}