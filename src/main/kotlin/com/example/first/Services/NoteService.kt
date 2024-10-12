package com.example.first.Services

import com.example.first.database.dto.NoteDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.User
import com.example.first.database.tables.NotesTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NoteService {

    suspend fun createNote(note: NoteDto): Note {
        return Note.new {
            this.title = note.title
            this.text = note.text
            this.user = User[note.userId]
        }
    }

    suspend fun getNote(id: UUID): Note {
        return Note[id]
    }

    suspend fun getNote(title: String): Note {
        val note = Note.find {
                NotesTable.title eq title
            }.firstOrNull()

        require(note != null) { "Заметки с названием $title не существует" }
        return note
    }

    suspend fun getUserNotes(userId: UUID): List<NoteDto> {
        return Note.find { NotesTable.user eq userId }.map { it.toDto() }.toList()
    }

    suspend fun updateNote(title: String, text: String): Note {
        val note = getNote(title)
        note.text = text
        return note
    }

    suspend fun deleteNote(title: String) {
        NotesTable.deleteWhere { NotesTable.title eq title }
    }

}