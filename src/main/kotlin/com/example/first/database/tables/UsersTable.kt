package com.example.first.database.tables

import com.example.first.configuration.Config
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable

object UsersTable: UUIDTable("${Config.schemaName}.users") {
    val email = varchar("email",200).uniqueIndex()
    val name = varchar("name", 100)
    val surname = varchar("surname", 100)
    val patronymic = varchar("patronymic", 100)
    val password = varchar("password", 200)
}