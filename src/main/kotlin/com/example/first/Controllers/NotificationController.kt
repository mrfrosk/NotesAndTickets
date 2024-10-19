package com.example.first.Controllers

import com.example.first.Services.NotificationService
import com.example.first.Services.dto.NewNotificationDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    suspend fun createNotification(@RequestBody notification: String): ResponseEntity<*> {
        val newNotificationDto = Json.decodeFromString<NewNotificationDto>(notification)
        val newNotification = notificationService.createNotification(newNotificationDto)
        return ResponseEntity.ok().body(Json.encodeToString(newNotification))
    }

    @GetMapping("/{id}")
    suspend fun getNoteNotifications(@PathVariable id: UUID): ResponseEntity<*> {
        val notifications = newSuspendedTransaction {
            notificationService.getNoteNotifications(id)
        }
        return ResponseEntity.ok().body(Json.encodeToString(notifications))
    }

    @DeleteMapping("/{noteId}")
    suspend fun deleteNotifications(@PathVariable noteId: UUID): ResponseEntity<*> {
        newSuspendedTransaction {
            notificationService.deleteNotifications(noteId)
        }
        return ResponseEntity.ok().body(null)
    }

    @DeleteMapping("/notification/{notificationId}")
    suspend fun deleteNotification(@PathVariable notificationId: UUID): ResponseEntity<*> {
        newSuspendedTransaction {
            notificationService.deleteNotification(notificationId)
        }
        return ResponseEntity.ok().body(null)
    }
}