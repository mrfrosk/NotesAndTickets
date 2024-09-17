package com.example.first.configuration

import org.jetbrains.exposed.sql.Schema

object Config {
    const val SCHEMA_NAME = "work"
    val schema = Schema(SCHEMA_NAME)
}