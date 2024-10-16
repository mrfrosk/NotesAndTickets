package com.example.first.Services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NotificationScheduler {
    @Autowired
    lateinit var notificationService: NotificationService

    @Scheduled(cron = "@daily")
    fun notification(){
        notificationService.sendDailyNotification()

    }


}