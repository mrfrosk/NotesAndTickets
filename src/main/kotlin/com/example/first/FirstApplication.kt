package com.example.first

import com.example.first.Services.utils.Hashing
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@ImportAutoConfiguration(
	value = [ExposedAutoConfiguration::class],
	exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class FirstApplication

fun main(args: Array<String>) {
	runApplication<FirstApplication>(*args)
}
