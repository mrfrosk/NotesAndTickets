package com.example.first.configuration

import org.jetbrains.exposed.sql.Schema

object Config {
    val schemaName = "work"
    val schema = Schema(schemaName)
}