package com.example.first.controller_test

import com.example.first.Controllers.Mapping
import com.example.first.FirstApplication
import com.example.first.Services.dto.AuthDto
import com.example.first.Services.dto.TokensDto
import com.example.first.Services.utils.Hashing
import com.example.first.Services.dto.NewUserDto
import com.example.first.database.dto.UserDto
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import kotlin.test.Test
import kotlin.test.assertEquals


@SpringBootTest(
    webEnvironment = WebEnvironment.DEFINED_PORT,
    classes = [FirstApplication::class]
)
class UserControllerTest(
) {
    val serverAddress = "http://localhost:8080"
    val client = HttpClient(CIO)
    val newUser = NewUserDto("test@Mail.com", "test", "test", "test", "123")
    val userMail = "test"
    @BeforeEach
    fun init(): Unit = runBlocking {
        newSuspendedTransaction {
            User.new {
                email = userMail
                name = "test"
                surname = "test"
                patronymic = "test"
                password = Hashing.toSha256("123")
            }

            User.new {
                email = "test1"
                name = "test"
                surname = "test"
                patronymic = "test"
                password = Hashing.toSha256("123")
            }
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun createUser(): Unit = runBlocking {
        val createRequest = client.post("$serverAddress${Mapping.USERS}/new") {
            body = Json.encodeToString(newUser)
        }
        val user = Json.decodeFromString<UserDto>(createRequest.bodyAsText())
        val dbUser = newSuspendedTransaction {
            User.find { UsersTable.email eq newUser.email }.firstOrNull()?.toDto()
        }
        assertEquals(HttpStatusCode.OK, createRequest.status)
        assertEquals(dbUser, user)

    }

    @Test
    fun getUser(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.get("$serverAddress${Mapping.USERS}/user/$userMail"){
            headers.append("Authorization", "Bearer $accessToken")
        }
        val user = Json.decodeFromString<UserDto>(request.bodyAsText())
        println("$serverAddress${Mapping.USERS}/user/$userMail")
        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(getUserFromBd(), user)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getUsers(): Unit = runBlocking {
        val accessToken = getAccessToken()
        val request = client.get("$serverAddress${Mapping.USERS}/all"){
            headers.append("Authorization", "Bearer $accessToken")
        }
        val users = Json.decodeFromString<List<UserDto>>(request.bodyAsText())
        val dbUsers = newSuspendedTransaction {
            User.all().map { it.toDto() }
        }
        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(dbUsers, users)
    }

    fun getUserFromBd(): UserDto = transaction {
        User.find { UsersTable.email eq userMail }.first().toDto()
    }

    @OptIn(InternalAPI::class)
    fun getAccessToken() = runBlocking {
        val authRequest = client.post("$serverAddress${Mapping.AUTH}/login") {
            body = Json.encodeToString(AuthDto("test", "123"))
        }.bodyAsText()
        return@runBlocking Json.decodeFromString<TokensDto>(authRequest).accessToken
    }

    @AfterEach
    fun clear(): Unit = transaction {
        UsersTable.deleteAll()
    }

}