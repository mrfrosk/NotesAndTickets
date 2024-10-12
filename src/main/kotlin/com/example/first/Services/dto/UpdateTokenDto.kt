package com.example.first.Services.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenDto(val token: String, val email: String)
