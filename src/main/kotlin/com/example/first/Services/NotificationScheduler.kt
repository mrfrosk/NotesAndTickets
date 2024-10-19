package com.example.first.Services

import com.example.first.Services.utils.MailSender
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NotificationScheduler {
    @Autowired
    lateinit var notificationService: NotificationService
    @Autowired
    lateinit var sender: MailSender

    @Scheduled(cron = "@daily")
    fun sendDailyNotification() {
        val notifications = notificationService.getDailyNotifications()

        notifications.forEach {
            sender.send(it.second, it.first, "уведомление")
        }
    }


}