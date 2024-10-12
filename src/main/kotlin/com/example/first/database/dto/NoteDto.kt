package com.example.first.database.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NoteDto(
    @SerialName("title")
    val title: String,
    @SerialName("test")
    val text: String,
    @SerialName("user-id")
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID?=null
)
