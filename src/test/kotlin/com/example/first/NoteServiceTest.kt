package com.example.first

import com.example.first.Services.NoteService
import com.example.first.Services.UserService
import com.example.first.database.dto.NoteDto
import com.example.first.database.dto.NewUserDto
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


    @Test
    fun clearData() {
        transaction {
            NotesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }
}