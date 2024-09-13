package com.example.first

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@ImportAutoConfiguration(
	exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class FirstApplication

fun main(args: Array<String>) {
	runApplication<FirstApplication>(*args)
}
