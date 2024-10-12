package com.example.first.Controllers

import com.example.first.Services.NotificationService
import com.example.first.database.dto.NotificationDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.not
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
    suspend fun createNotification(@RequestBody notification: String): ResponseEntity<Nothing> {
        val notificationDto = Json.decodeFromString<NotificationDto>(notification)
        notificationService.createNotification(notificationDto)
        return ResponseEntity.ok().body(null)
    }

    @GetMapping("/{id}")
    suspend fun getNotifications(@PathVariable id: UUID): ResponseEntity<String> {
        val notifications = notificationService.getUserNotifications(id)
        return ResponseEntity.ok().body(Json.encodeToString(notifications))
    }

    @DeleteMapping("/{noteId}")
    suspend fun deleteNotifications(@PathVariable noteId: UUID){
        newSuspendedTransaction {
            notificationService.deleteNotifications(noteId)
        }
    }
    @DeleteMapping("/notification/{notificationId}")
    suspend fun deleteNotification(@PathVariable notificationId: UUID){
        newSuspendedTransaction {
            notificationService.deleteNotification(notificationId)
        }
    }
}