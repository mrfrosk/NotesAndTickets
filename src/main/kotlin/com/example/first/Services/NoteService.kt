package com.example.first.Services

import com.example.first.database.dto.NoteDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.User
import com.example.first.database.tables.NotesTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NoteService {

    fun createNote(note: NoteDto): Note {

        return transaction {
            Note.new {
                this.title = note.title
                this.text = note.text
                this.user = User[note.userId]
            }
        }

    }

    fun getNote(id: UUID): Note {
        return transaction {
            Note[id]
        }
    }

    fun getNote(title: String): Note {
        val note = transaction {
            Note.find {
                NotesTable.title eq title
            }.firstOrNull()
        }
        require(note != null) { "Заметки с названием $title не существует" }
        return note
    }

    fun getUserNotes(userId: UUID): List<NoteDto> {
        return transaction {
            Note.find { NotesTable.user eq userId }.map { it.toDto() }
        }.toList()

    }

    fun updateNote(title: String, text: String): Note {
        val note = getNote(title)
        transaction {
            note.text = text
        }
        return note
    }

    fun deleteNote(title: String){
        transaction {
            NotesTable.deleteWhere { NotesTable.title eq title }
        }
    }

}