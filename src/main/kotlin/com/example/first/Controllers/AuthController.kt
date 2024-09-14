package com.example.first.Controllers

import com.example.first.Services.AuthenticationService
import com.example.first.Services.dto.AuthenticationRequest
import com.example.first.Services.dto.AuthenticationResponse
import com.example.first.Services.dto.RefreshTokenRequest
import com.example.first.Services.dto.TokenResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController{
    @Autowired
    lateinit var authenticationService: AuthenticationService
    @PostMapping
    fun authenticate(
        @RequestBody authRequest: AuthenticationRequest
    ): AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): TokenResponse =
        authenticationService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token.")
    private fun String.mapToTokenResponse(): TokenResponse =
        TokenResponse(
            token = this
        )
}