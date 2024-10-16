package com.example.first.service_test

import com.example.first.Services.NotificationService
import com.example.first.database.dto.NewNotificationDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.Notification
import com.example.first.database.entities.User
import com.example.first.database.tables.NotesTable
import com.example.first.database.tables.NotificationsTable
import com.example.first.database.tables.UsersTable
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class NotificationServiceTest {
    @Autowired
    lateinit var notificationService: NotificationService

    final val currentDay = LocalDateTime.now().toKotlinLocalDateTime()
    val userId = UUID.randomUUID()
    final val noteId = UUID.randomUUID()
    val notificationId = UUID.randomUUID()
    val notificationDto = NewNotificationDto("text", currentDay, false, noteId)
    val nextDate = (Instant.now() + Duration.ofDays(2)).toKotlinInstant().toLocalDateTime(TimeZone.UTC)

    @BeforeEach
    fun initData(): Unit = transaction {
        User.new(userId) {
            email = "test@mail.ru"
            name = "name"
            surname = "surname"
            patronymic = "patronymic"
            password = "password"
        }

        Note.new(noteId) {
            title = "title1"
            text = "test"
            user = User[userId]
        }

        Notification.new(notificationId) {
            text = "text1"
            date = currentDay
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

    @Test
    fun createNotification(): Unit = runBlocking {
        newSuspendedTransaction {
            val notification = notificationService.createNotification(notificationDto)
            val dbNotification = Notification.find { NotificationsTable.text eq notificationDto.text }.first().toDto()
            assertEquals(dbNotification, notification)
        }
    }

    @Test
    fun getDailyNotification(): Unit = runBlocking {
        newSuspendedTransaction {
            val dailyNotifications = notificationService.getDailyNotifications()
            assertEquals(1, dailyNotifications.size)
            assertEquals(Notification[notificationId].toDto(), dailyNotifications.first().toDto())
        }
    }

    @Test
    fun getNoteNotifications(): Unit = runBlocking {
        newSuspendedTransaction {
            val notifications = notificationService.getUserNotifications(noteId)
            val bdNotifications = Notification.find { NotificationsTable.noteId eq noteId }.map { it.toDto() }
            assertEquals(bdNotifications, notifications)
        }
    }

    @Test
    fun deleteNotification(): Unit = runBlocking {
        newSuspendedTransaction {
            notificationService.deleteNotification(notificationId)
            assertThrows<EntityNotFoundException> {
                Notification[notificationId]
            }
        }
    }

    @Test
    fun deleteNotifications(): Unit = runBlocking {
        newSuspendedTransaction {
            notificationService.deleteNotifications(noteId)
            assertEquals(0, notificationService.getUserNotifications(noteId).size)
        }
    }

    @AfterEach
    fun clear() {
        transaction {
            NotesTable.deleteAll()
            UsersTable.deleteAll()
            NotificationsTable.deleteAll()
        }
    }
}