package com.example.first.Services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  NewUserDto(
    @SerialName("email")
    var email: String,
    @SerialName("name")
    var name: String,
    @SerialName("surname")
    var surname: String,
    @SerialName("patronymic")
    var patronymic: String,
    @SerialName("password")
    var password: String
)
