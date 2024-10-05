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

    @Scheduled(cron = "@daily")
    fun notification(){
        println("hello world")
        notificationService.sendDailyNotification()
        println("goodbye world")

    }
}