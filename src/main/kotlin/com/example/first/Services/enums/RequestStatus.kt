package com.example.first.Services.enums

import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus(val description: String) {
    AuthSuccess("успешно"),
    AuthFailed("не верный логин или пароль"),
    NoteDoestNoExists("заметки с таким id или title не существует"),
    NoteExists("нельзя создать данную заметку, так как она уже существует")
}