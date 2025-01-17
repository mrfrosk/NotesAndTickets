package com.example.first.database.tables

import com.example.first.configuration.Config
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object NotesTable: UUIDTable("${Config.SCHEMA_NAME}.notes") {
    val title = varchar("title",200).uniqueIndex()
    val text = text("text")
    val user = reference("user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}