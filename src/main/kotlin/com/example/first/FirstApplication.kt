package com.example.first

import com.example.first.configuration.yaml.JwtProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
@ImportAutoConfiguration(
	exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class FirstApplication

fun main(args: Array<String>) {
	runApplication<FirstApplication>(*args)
}
