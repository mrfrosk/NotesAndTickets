package com.example.first.database.confguration

import org.jetbrains.exposed.sql.Schema

object Config {
    val schemaName = "work"
    val schema = Schema(schemaName)
}