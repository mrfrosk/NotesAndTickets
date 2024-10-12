package com.example.first.database.dto

import com.example.first.Services.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NoteDto(
    val title: String,
    val text: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID?=null
)
