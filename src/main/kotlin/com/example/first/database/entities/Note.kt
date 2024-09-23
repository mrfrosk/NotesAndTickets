package com.example.first.database.entities

import com.example.first.database.dto.NoteDto
import com.example.first.database.tables.NotesTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

data class Note(val _id: EntityID<UUID>) : Entity<UUID>(_id) {
    companion object : EntityClass<UUID, Note>(NotesTable)

    var title by NotesTable.title
    var text by NotesTable.text
    var user by User referencedOn NotesTable.user
    fun toDto() = transaction { NoteDto(title, text, user.id.value, _id.value) }
}
