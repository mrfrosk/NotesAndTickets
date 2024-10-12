package com.example.first.Services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenDto(
    @SerialName("token")
    val token: String,
    @SerialName("email")
    val email: String)
