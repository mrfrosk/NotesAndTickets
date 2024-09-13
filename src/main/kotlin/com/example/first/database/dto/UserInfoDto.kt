package com.example.first.database.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserInfoDto(
    val name: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)
