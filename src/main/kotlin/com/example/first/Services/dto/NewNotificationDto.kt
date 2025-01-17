package com.example.first.Services.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NewNotificationDto(
    @SerialName("text")
    val text: String,
    @SerialName("date")
    val date: LocalDateTime,
    @SerialName("repeat")
    val repeat: Boolean,
    @SerialName("note-id")
    @Serializable(with = UUIDSerializer::class)
    val noteId: UUID
)
