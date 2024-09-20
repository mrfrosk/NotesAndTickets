package com.example.first.database.tables

import com.example.first.configuration.Config
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object NotificationsTable: UUIDTable("${Config.SCHEMA_NAME}.notifications"){
    val text = varchar("text",200)
    val date = datetime("date")
    val repeat = bool("repeat")
    val noteId = reference("note-id", NotesTable.id)
}