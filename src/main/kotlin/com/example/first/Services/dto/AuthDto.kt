package com.example.first.Services.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(val email: String, val password: String)
