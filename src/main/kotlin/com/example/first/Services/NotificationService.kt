package com.example.first.Services

import com.example.first.Services.utils.MailSender
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
import kotlin.time.toKotlinDuration

@Service
class NotificationService {
    @Autowired
    lateinit var sender: MailSender
    fun createNotification(notification: NotificationDto) {
        transaction {
            Notification.new {
                this.text = notification.text
                this.date = notification.date
                this.repeat = notification.repeat
                this.note = Note[notification.noteId]
            }
        }
    }

    fun getDailyNotifications(): List<Notification> {
        val startTime = getMidnightTime()
        val endTime = startTime.toInstant(TimeZone.UTC) + Duration.ofDays(2).toKotlinDuration()
        return transaction {
            Notification.find {
                (NotificationsTable.date greaterEq startTime) and
                        (NotificationsTable.date less endTime.toLocalDateTime(TimeZone.UTC))
            }.toList()
        }
    }

    fun getMidnightTime(): LocalDateTime {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val midnightTime = LocalDateTime(currentDateTime.date, LocalTime(0, 0))
        return midnightTime
    }

    fun sendDailyNotification() {
        val notifications = getDailyNotifications()
        val sandedNotify = transaction { notifications.map { it.text to it.note.user.email } }
        println("кооличество уведмоленией ${notifications.size}")
        sandedNotify.forEach {
            println("отправка началась")
            sender.send(it.second, it.first, "уведомление")
            println("отправка должна закончиться")
        }
    }

}