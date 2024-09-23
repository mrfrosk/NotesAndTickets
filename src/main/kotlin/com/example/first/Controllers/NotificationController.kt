package com.example.first.Controllers

import com.example.first.Services.NotificationService
import com.example.first.database.dto.NotificationDto
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Mapping.NOTIFICATIONS)
class NotificationController {
    @Autowired
    lateinit var notificationService: NotificationService

    @PostMapping("/new")
    fun createNotification(@RequestBody notification: String): Boolean {
        val notificationDto = Json.decodeFromString<NotificationDto>(notification)
        notificationService.createNotification(notificationDto)
        return true
    }
}