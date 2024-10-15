package com.example.first

import com.example.first.Services.NotificationService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NotificationServiceTest {
    @Autowired
    lateinit var notificationService: NotificationService


    @Test
    fun test1(){
//        val uuid = UUID.fromString("031262e9-b232-40e7-876e-d47bc691e5e8")
//        val time = Clock.System.now().toLocalDateTime(TimeZone.UTC)
//        val notification = NotificationDto("asdasd", time, false, uuid)
//        notificationService.createNotification(notification)
    }
}