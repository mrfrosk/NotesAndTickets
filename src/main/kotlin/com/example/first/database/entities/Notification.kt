package com.example.first.database.entities

import com.example.first.database.dto.NewNotificationDto
import com.example.first.database.dto.NotificationDto
import com.example.first.database.tables.NotificationsTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class Notification(val _id: EntityID<UUID>) : Entity<UUID>(_id) {
    companion object : EntityClass<UUID, Notification>(NotificationsTable)
    var text by NotificationsTable.text
    var date by NotificationsTable.date
    var repeat by NotificationsTable.repeat
    var note by Note referencedOn NotificationsTable.noteId

    fun toDto() = NotificationDto(text, date, repeat, id.value, note.id.value)
}