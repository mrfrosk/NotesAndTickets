package com.example.first.controller_test

import com.example.first.Controllers.Mapping
import com.example.first.database.dto.NewUserDto
import com.example.first.database.tables.UsersTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class UserControllerTest {
    val serverAddress = "http://localhost:8080"
    val client = HttpClient(CIO)
    val newUser = NewUserDto("test@Mail.com", "test", "test", "test", "123")

    @OptIn(InternalAPI::class)
    @Test
    fun createUser(): Unit = runBlocking {

        val request = client.post("$serverAddress${Mapping.USERS}/new"){
            body = Json.encodeToString(newUser)
        }

        val request2 = client.post("$Mapping.USERS/new"){
            body = Json.encodeToString(newUser)
        }

        assertEquals(HttpStatusCode.OK, request.status)
    }

    @AfterEach
    fun clear(): Unit = transaction{
        UsersTable.deleteAll()
    }

}