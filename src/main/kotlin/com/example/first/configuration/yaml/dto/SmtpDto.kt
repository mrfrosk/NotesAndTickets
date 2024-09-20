package com.example.first.configuration.yaml.dto

data class SmtpDto(
    val address: String,
    val port: Int,
    val username: String,
    val password: String
)