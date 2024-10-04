package com.example.first.Services.enums

import kotlinx.serialization.Serializable

@Serializable
enum class LoginStatus(val description: String) {
    Success("успешно"),
    Denied("не успешно")
}