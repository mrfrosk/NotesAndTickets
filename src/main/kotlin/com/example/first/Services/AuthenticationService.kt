package com.example.first.Services


import com.example.first.Services.dto.AuthenticationRequest
import com.example.first.Services.dto.AuthenticationResponse
import com.example.first.configuration.yaml.JwtProperties
import com.example.first.database.dto.UserInfoDto
import com.example.first.database.entities.User
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun authentication(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password
            )
        )

        val user = userService.getUser(authenticationRequest.email)

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(transaction { user.toInfoDto() })
        refreshTokenRepository.save(refreshToken, transaction { user.toInfoDto() })

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
    fun refreshAccessToken(refreshToken: String): String? {
        val extractedEmail = tokenService.extractEmail(refreshToken)
        return extractedEmail?.let { email ->
            val currentUserDetails = userService.getUser(email)
            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(refreshToken)
            if (!tokenService.isExpired(refreshToken) && refreshTokenUserDetails?.email == currentUserDetails.email)
                createAccessToken(currentUserDetails)
            else
                null
        }
    }

    private fun createAccessToken(user: User) = tokenService.generate(
        user = user.toInfoDto(),
        expirationDate = getAccessTokenExpiration()
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

    private fun createRefreshToken(user: UserInfoDto) = tokenService.generate(
        user = user,
        expirationDate = getRefreshTokenExpiration()
    )
    private fun getRefreshTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)

}