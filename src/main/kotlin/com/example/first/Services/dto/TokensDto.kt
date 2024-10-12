package com.example.first.Services.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokensDto(val accessToken: String, val refreshToken: String)
