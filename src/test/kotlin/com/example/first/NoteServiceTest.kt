package com.example.first

import com.example.first.Services.NoteService
import com.example.first.Services.UserService
import com.example.first.database.dto.NoteDto
import com.example.first.database.dto.UserFullDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.User
import com.example.first.database.tables.NotesTable
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class NoteServiceTest {
    @Autowired
    lateinit var noteService: NoteService

    @Autowired
    lateinit var userService: UserService

    final val userId: UUID = UUID.randomUUID()
    val noteId: UUID = UUID.randomUUID()
    val userFullDto = UserFullDto(
        "testMail1",
        "testName1",
        "testSurname1",
        "testPatronymic1",
        "sadasdasd"
    )
    val noteDto = NoteDto(
        "testTitle1",
        "lorem ipsum expam on this same ",
        userId
    )


    @BeforeEach
    fun initData() {
        transaction {
            User.new(userId) {
                this.email = userFullDto.email
                this.name = userFullDto.name
                this.surname = userFullDto.surname
                this.patronymic = userFullDto.patronymic
                this.password = userFullDto.password
            }
            Note.new(noteId) {
                this.title = noteDto.title
                this.text = noteDto.text
                this.user = User[userId]
            }
        }
    }

    @Test
    fun createNote() {
        val dto = NoteDto("asda", "asdas", userId)
        val note  = noteService.createNote(dto).toDto()
        assertEquals(dto, note)
    }


    @Test
    fun getNote() {
        transaction {  }
        val note = transaction {  noteService.getNote(noteId).toDto() }
        assertEquals(noteDto, note)
    }

    @Test
    fun getUserNotes(){
        transaction {
            noteService.getUserNotes(userId)
        }
    }

    @AfterEach
    fun clearData() {
        transaction {
            NotesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }
}