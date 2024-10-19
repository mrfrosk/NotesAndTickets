package com.example.first.Services.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNotificationDto(
    @SerialName("old-text")
    val text: String,
    @SerialName("date")
    val date: LocalDateTime,
    @SerialName("repeat")
    val repeat: Boolean
)
