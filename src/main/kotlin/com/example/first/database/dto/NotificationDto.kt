package com.example.first.database.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NotificationDto(
    val text: String,
    val date: LocalDateTime,
    val repeat: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val noteId: UUID
)
