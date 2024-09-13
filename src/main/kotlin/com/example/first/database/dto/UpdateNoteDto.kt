package com.example.first.database.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNoteDto(val title: String, val text: String)
