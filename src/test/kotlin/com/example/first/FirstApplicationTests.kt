package com.example.first

import com.auth0.jwt.JWT
import com.example.first.Services.JwtService
import com.example.first.Services.NotificationService
import com.example.first.Services.utils.MailSender
import io.jsonwebtoken.Jwt
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component
import kotlin.concurrent.thread

@SpringBootTest
class FirstApplicationTests {

    @Autowired
    lateinit var sender: MailSender


    fun test1(){
        sender.send("heroker44@gmail.com", "Все ок, работает температура +50, все как мы любим")
    }


}


