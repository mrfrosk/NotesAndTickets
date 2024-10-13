package com.example.first.Services.enums

import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus(val description: String) {
    Success("успешно"),
    Denied("не верный логин или пароль")
}