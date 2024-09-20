package com.example.first.Services

import com.example.first.database.dto.NotificationDto
import com.example.first.database.entities.Note
import com.example.first.database.entities.Notification
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class NotificationService {

    fun createNotification(notification: NotificationDto){
        transaction {
            Notification.new {
                this.text = notification.text
                this.date = notification.date
                this.repeat = notification.repeat
                this.note = Note[notification.noteId]
            }
        }
    }
}