package com.example.first.Services

import com.example.first.Services.utils.MailSender
import com.example.first.database.dto.NewNotificationDto
import com.example.first.database.dto.NotificationDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.Notification
import com.example.first.database.tables.NotificationsTable
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.Duration
import kotlinx.datetime.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID
import kotlin.time.toKotlinDuration

@Service
class NotificationService {

    fun createNotification(notification: NewNotificationDto): NotificationDto {
        return transaction {
            Notification.new {
                this.text = notification.text
                this.date = notification.date
                this.repeat = notification.repeat
                this.note = Note[notification.noteId]
            }.toDto()
        }
    }

    fun getDailyNotifications(): List<Pair<String, String>> {
        val startTime = getMidnightTime()
        val endTime = startTime.toInstant(TimeZone.UTC) + Duration.ofDays(2).toKotlinDuration()
        return transaction {
            Notification.find {
                (NotificationsTable.date greaterEq startTime) and
                        (NotificationsTable.date less endTime.toLocalDateTime(TimeZone.UTC))
            }.toList().map { it.text to it.note.user.email }
        }
    }

    fun getNoteNotifications(id: UUID): List<NotificationDto> {
        return Notification.find { NotificationsTable.noteId eq id }.map { it.toDto() }
    }

    private fun getMidnightTime(): LocalDateTime {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val midnightTime = LocalDateTime(currentDateTime.date, LocalTime(0, 0))
        return midnightTime
    }



    suspend fun deleteNotifications(noteId: UUID) {
        val notifications = Notification.find { NotificationsTable.noteId eq noteId }
        notifications.forEach {
            it.delete()
        }

    }

    suspend fun deleteNotification(notificationId: UUID) {
        Notification[notificationId].delete()
    }

}