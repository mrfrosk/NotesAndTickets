package com.example.first.database.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(val email: String, val password: String)
