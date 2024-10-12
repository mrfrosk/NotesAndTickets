package com.example.first.Controllers

import com.example.first.Services.NotificationService
import com.example.first.database.dto.NotificationDto
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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

    @GetMapping("/{id}")
    fun getNotifications(@PathVariable id: UUID): List<NotificationDto> {
        return notificationService.getUserNotifications(id)
    }

    @DeleteMapping("/{noteId}")
    suspend fun deleteNotifications(@PathVariable noteId: UUID){
        notificationService.deleteNotifications(noteId)
    }
    @DeleteMapping("/notification/{notificationId}")
    suspend fun deleteNotification(@PathVariable notificationId: UUID){
        notificationService.deleteNotification(notificationId)
    }
}