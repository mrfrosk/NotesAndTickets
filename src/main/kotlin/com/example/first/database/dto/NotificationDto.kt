package com.example.first.database.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NotificationDto(
    @SerialName("text")
    val text: String,
    @SerialName("date")
    val date: LocalDateTime,
    @SerialName("repeat")
    val repeat: Boolean,
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    @SerialName("note-id")
    @Serializable(with = UUIDSerializer::class)
    val noteId: UUID
)
