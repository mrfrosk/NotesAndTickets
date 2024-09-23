package com.example.first

import com.example.first.Services.NotificationService
import com.example.first.Services.utils.MailSender
import com.example.first.database.entities.Notification
import com.example.first.database.tables.NotificationsTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

@SpringBootTest
class FirstApplicationTests {

    @Autowired
    lateinit var sender: MailSender

    @Autowired
    lateinit var notificationService: NotificationService
    val client = HttpClient(CIO)
    val serverAddress = "http://localhost:8080/api/suspend"

    @Test
    fun send() {
        sender.send("heroker44@gmail.com", "Можно релизить", "Оповещение")
    }

    @Test
    fun time() {
        println(notificationService.getMidnightTime())
        println(notificationService.getDailyNotifications().size)
    }

    @Test
    fun suspendTest(){
        runBlocking {
            val request = client.get(serverAddress)
            println(request.status)
        }
    }


}
