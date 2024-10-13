package com.example.first

import com.example.first.Services.UserService
import com.example.first.Services.utils.Hashing
import com.example.first.database.dto.NewUserDto
import com.example.first.database.tables.UsersTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserServiceTest {

    @Autowired
    lateinit var userService: UserService
    val newUser = NewUserDto(
        "email",
        "name",
        "surname",
        "patronymic",
        "password"
    )

    @Test
    fun createUser() {
        runBlocking {
            val user = newSuspendedTransaction {
                userService.createUser(newUser)
            }
            assertEquals(newUser.email, user.email)
            assertEquals(newUser.name, user.name)
            assertEquals(newUser.surname, user.surname)
            assertEquals(newUser.patronymic, user.patronymic)
        }
    }

    @AfterEach
    fun clear() {
        transaction {
            UsersTable.deleteAll()
        }
    }
}