package com.example.first.service_test

import com.example.first.Services.NoteService
import com.example.first.database.dto.NewNoteDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.User
import com.example.first.database.tables.NotesTable
import com.example.first.database.tables.UsersTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class NoteServiceTest {
    @Autowired
    lateinit var noteService: NoteService

    final val userId = UUID.randomUUID()
    final val noteId = UUID.randomUUID()
    val newNote = NewNoteDto("title", "text", userId)
    val noteTitle = "dbTitle"
    val newTitle = "dbTitle2"

    @BeforeEach
    fun initData(): Unit = runBlocking {
        newSuspendedTransaction {
            User.new(userId) {
                email = "test"
                name = "test"
                surname = "test"
                patronymic = "test"
                password = "test"
            }

            Note.new(noteId) {
                title = noteTitle
                text = "dbText"
                user = User[userId]
            }
            Note.new {
                title = "dbtitle1"
                text = "dbText"
                user = User[userId]
            }
        }
    }

    @Test
    fun createNote(): Unit = runBlocking {
        newSuspendedTransaction {
            val note = noteService.createNote(newNote)?.toDto()
            val dbNote = Note.find {
                NotesTable.title eq newNote.title
            }.firstOrNull()?.toDto()

            assertEquals(dbNote, note)
        }
    }

    @Test
    fun getById(): Unit = runBlocking {
        newSuspendedTransaction {
            val note = noteService.getNote(noteId).toDto()
            val dbNote = Note[noteId].toDto()
            assertEquals(dbNote, note)
        }
    }

    @Test
    fun getByTitle(): Unit = runBlocking {
        newSuspendedTransaction {
            val note = noteService.getNote(noteTitle)?.toDto()
            val dbNote = Note.find { NotesTable.title eq noteTitle }.first().toDto()
            assertEquals(dbNote, note)
        }
    }

    @Test
    fun getNotesByUserId(): Unit = runBlocking {
        newSuspendedTransaction {
            val notes = noteService.getUserNotes(userId)
            val dbNotes = Note.find { NotesTable.user eq userId }.map { it.toDto() }
            assertEquals(dbNotes, notes)
        }
    }

    @Test
    fun updateNote(): Unit = runBlocking {
        newSuspendedTransaction {
            val updatedNote = noteService.updateNoteV2(noteTitle, newTitle, "new text")
            val dbNote = Note[noteId].toDto()
            assertEquals(dbNote, updatedNote)
        }

    }

    @Test
    fun deleteNote(): Unit = runBlocking {
        newSuspendedTransaction {
            noteService.deleteNote(noteTitle)
            assertThrows<IllegalArgumentException> {
                noteService.getNote(noteTitle)
            }
        }
    }

    @AfterEach
    fun clearData(): Unit = transaction {
        NotesTable.deleteAll()
        UsersTable.deleteAll()
    }
}