package com.example.first.controller_test

import com.example.first.Controllers.Mapping
import com.example.first.Services.dto.AuthDto
import com.example.first.Services.dto.TokensDto
import com.example.first.Services.utils.Hashing
import com.example.first.Services.dto.NewNotificationDto
import com.example.first.database.dto.NotificationDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.Notification
import com.example.first.database.entities.User
import com.example.first.database.tables.NotificationsTable
import com.example.first.database.tables.UsersTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import java.time.Duration
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.toKotlinDuration

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class NotificationControllerTest {
    val serverAddress = "http://localhost:8080"
    val client = HttpClient(CIO)
    val userId = UUID.randomUUID()
    val noteId = UUID.randomUUID()
    val notificationId = UUID.randomUUID()
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    val nextDate = (Clock.System.now() + Duration.ofDays(2).toKotlinDuration()).toLocalDateTime(TimeZone.UTC)
    val newNotification = NewNotificationDto("test", currentDate, false, noteId)

    @BeforeEach
    fun init(): Unit = transaction {
        User.new(userId) {
            email = "test"
            name = "test"
            surname = "test"
            patronymic = "test"
            password = Hashing.toSha256("123")
        }

        Note.new(noteId) {
            title = "test"
            text = "test"
            user = User[userId]
        }

        Notification.new(notificationId) {
            text = "text1"
            date = currentDate
            repeat = false
            note = Note[noteId]
        }

        Notification.new {
            text = "text2"
            date = nextDate
            repeat = false
            note = Note[noteId]
        }
    }


    @OptIn(InternalAPI::class)
    @Test
    fun createNotification(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.post("$serverAddress${Mapping.NOTIFICATIONS}/new") {
            body = Json.encodeToString(newNotification)
            headers.append("Authorization", "Bearer $accessToken")
        }
        val notification = Json.decodeFromString<NotificationDto>(request.bodyAsText())
        val dbNotification = newSuspendedTransaction {
            Notification.find { NotificationsTable.text eq newNotification.text }.first().toDto()
        }

        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(dbNotification, notification)
    }

    @Test
    fun deleteNotification(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.delete("$serverAddress${Mapping.NOTIFICATIONS}/notification/$notificationId") {
            headers.append("Authorization", "Bearer $accessToken")
        }
        assertEquals(HttpStatusCode.OK, request.status)
        assertThrows<EntityNotFoundException> {
            transaction { Notification[notificationId] }
        }
    }

    @Test
    fun getNoteNotifications(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.get("$serverAddress${Mapping.NOTIFICATIONS}/${noteId}") {
            headers.append("Authorization", "Bearer $accessToken")
        }
        assertEquals(HttpStatusCode.OK, request.status)
        val notifications = Json.decodeFromString<List<NotificationDto>>(request.bodyAsText())
        val dbNotifications = newSuspendedTransaction {
            Notification.find { NotificationsTable.noteId eq noteId }.map { it.toDto() }
        }
        assertEquals(true, notifications.isNotEmpty())
        assertEquals(true, dbNotifications.isNotEmpty())
        notifications.zip(dbNotifications) { first, second -> assertNotifications(first, second) }
    }

    @Test
    fun deleteNotifications(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.delete("$serverAddress${Mapping.NOTIFICATIONS}/${noteId}") {
            headers.append("Authorization", "Bearer $accessToken")
        }
        val isEmpty = newSuspendedTransaction {
            Notification.find { NotificationsTable.noteId eq noteId }.toList().isEmpty()
        }
        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(true, isEmpty)
    }

    fun assertNotifications(first: NotificationDto, second: NotificationDto) {
        assertEquals(first.text, second.text)
        assertEquals(
            first.date.toInstant(TimeZone.UTC).epochSeconds,
            second.date.toInstant(TimeZone.UTC).epochSeconds
        )
        assertEquals(first.noteId, second.noteId)
        assertEquals(first.repeat, second.repeat)
        assertEquals(first.id, second.id)
    }

    @AfterEach
    fun clear(): Unit = transaction {
        UsersTable.deleteAll()
    }

    @OptIn(InternalAPI::class)
    fun getAccessToken() = runBlocking {
        val authRequest = client.post("$serverAddress${Mapping.AUTH}/login") {
            body = Json.encodeToString(AuthDto("test", "123"))
        }.bodyAsText()
        return@runBlocking Json.decodeFromString<TokensDto>(authRequest).accessToken
    }
}