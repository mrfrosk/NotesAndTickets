package com.example.first.Services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String)
