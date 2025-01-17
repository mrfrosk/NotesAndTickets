package com.example.first.configuration


import com.example.first.database.tables.NotesTable
import com.example.first.database.tables.NotificationsTable
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class SchemaInitializer : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        SchemaUtils.createSchema(Config.schema)
        SchemaUtils.createMissingTablesAndColumns(UsersTable, NotesTable, NotificationsTable)
    }
}