package com.example.first.Services

import com.example.first.database.dto.UserInfoDto
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
@Component
class RefreshTokenRepository {
    private val tokens = mutableMapOf<String, UserInfoDto>()
    fun findUserDetailsByToken(token: String) : UserInfoDto? =
        tokens[token]
    fun save(token: String, userDetails: UserInfoDto) {
        tokens[token] = userDetails
    }
}