package com.example.first.database.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserDto(
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("patronymic")
    val patronymic: String,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)
