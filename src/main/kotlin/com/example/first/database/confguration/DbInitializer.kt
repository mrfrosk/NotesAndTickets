package com.example.first.database.confguration


import com.example.first.database.tables.NotesTable
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class SchemaInitialize : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        SchemaUtils.createSchema(Config.schema)
        SchemaUtils.createMissingTablesAndColumns(UsersTable, NotesTable)
    }
}