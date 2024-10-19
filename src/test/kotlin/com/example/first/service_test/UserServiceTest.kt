package com.example.first.service_test

import com.example.first.Services.UserService
import com.example.first.Services.dto.NewUserDto
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserServiceTest {

    @Autowired
    lateinit var userService: UserService
    val newEmail = "email1"
    val newUser = NewUserDto(
        "email",
        "name",
        "surname",
        "patronymic",
        "password"
    )

    @BeforeEach
    fun init() {
        transaction {
            User.new {
                email = newEmail
                name = newUser.name
                surname = newUser.surname
                patronymic = newUser.patronymic
                password = newUser.password
            }
        }
    }

    @Test
    fun createUser() = runBlocking {
        newSuspendedTransaction {
            val user = userService.createUser(newUser)
            val userFromBd = User.find { UsersTable.email eq newUser.email }.first().toDto()
            assertEquals(userFromBd, user)
        }
    }


    @Test
    fun getUser() = runBlocking {
        newSuspendedTransaction {
            val user = userService.getUser(newEmail).toDto()
            val userFromBd = User.find {
                UsersTable.email eq newEmail
            }.first().toDto()
            assertEquals(userFromBd, user)
        }


    }

    @AfterEach
    fun clear() {
        transaction {
            UsersTable.deleteAll()
        }
    }
}