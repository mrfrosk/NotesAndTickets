package com.example.first.controller_test

import com.example.first.Controllers.Mapping
import com.example.first.FirstApplication
import com.example.first.Services.dto.AuthDto
import com.example.first.Services.dto.TokensDto
import com.example.first.Services.utils.Hashing
import com.example.first.database.dto.NewNoteDto
import com.example.first.database.dto.NoteDto
import com.example.first.database.dto.UpdateNoteDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.User
import com.example.first.database.tables.NotesTable
import com.example.first.database.tables.UsersTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest(
    webEnvironment = WebEnvironment.DEFINED_PORT,
    classes = [FirstApplication::class]
)
class NoteControllerTest {
    val serverAddress = "http://localhost:8080"
    val client = HttpClient(CIO)
    val userId = UUID.randomUUID()
    val noteId = UUID.randomUUID()
    val newNote = NewNoteDto("test", "test", userId)
    val updateDto = UpdateNoteDto("test1", "text1")

    @BeforeEach
    fun init() {
        transaction {
            User.new(userId) {
                email = "test"
                name = "test"
                surname = "test"
                patronymic = "test"
                password = Hashing.toSha256("123")
            }

            Note.new(noteId) {
                title = "title"
                text = "test"
                user = User[userId]
            }
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun createNote(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.post("$serverAddress${Mapping.NOTES}/note/new") {
            body = Json.encodeToString(newNote)
            headers.append("Authorization", "Bearer $accessToken")
        }
        val note = Json.decodeFromString<NoteDto>(request.bodyAsText())
        val dbNote = newSuspendedTransaction {
            Note.find {
                NotesTable.title eq newNote.title
            }.first().toDto()
        }

        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(dbNote, note)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getNote(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val requestById = client.get("$serverAddress${Mapping.NOTES}/note"){
            parameter("id", noteId)
            headers.append("Authorization", "Bearer $accessToken")
        }
        val requestByTitle = client.get("$serverAddress${Mapping.NOTES}/note") {
            parameter("title", "title")
            headers.append("Authorization", "Bearer $accessToken")
        }

        val noteByTitle = Json.decodeFromString<NoteDto>(requestByTitle.bodyAsText())
        val noteById = Json.decodeFromString<NoteDto>(requestById.bodyAsText())
        val dbNote = newSuspendedTransaction { Note.find { NotesTable.title eq "title" }.first().toDto() }
        assertEquals(dbNote, noteByTitle)
        assertEquals(dbNote, noteById)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun updateNote(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.put("http://localhost:8080/api/notes/noteV2/title") {
            body = Json.encodeToString(updateDto)
            headers.append("Authorization", "Bearer $accessToken")
        }
        println("v2 status: ${request.status}")
        println("$serverAddress${Mapping.NOTES}/noteV2/title")
        println(request.headers)
        val note = Json.decodeFromString<NoteDto>(request.bodyAsText())
        val dbNote = newSuspendedTransaction {
            Note[noteId].toDto()
        }
        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(dbNote, note)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun deleteNote(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.delete("$serverAddress${Mapping.NOTES}/note/title") {
            headers.append("Authorization", "Bearer $accessToken")
        }

        assertThrows<EntityNotFoundException> {
            transaction {
                Note[noteId]
            }
        }
        assertEquals(HttpStatusCode.OK, request.status)
    }


    @OptIn(InternalAPI::class)
    fun getAccessToken() = runBlocking {
        val authRequest = client.post("$serverAddress${Mapping.AUTH}/login") {
            body = Json.encodeToString(AuthDto("test", "123"))
        }.bodyAsText()
        return@runBlocking Json.decodeFromString<TokensDto>(authRequest).accessToken
    }

    @AfterEach
    fun clear(): Unit = transaction {
        UsersTable.deleteAll()
        NotesTable.deleteAll()
    }

}