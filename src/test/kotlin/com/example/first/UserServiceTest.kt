package com.example.first

import com.example.first.Services.UserService
import com.example.first.Services.utils.Hashing
import com.example.first.database.dto.NewUserDto
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
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
    fun createUser() = runBlocking {
        val user = newSuspendedTransaction {
            userService.createUser(newUser)
        }
        val userFromBd = newSuspendedTransaction {
            User.find { UsersTable.email eq newUser.email }.first().toDto()
        }
        assertEquals(userFromBd, user)
    }


    @Test
    fun getUser() = runBlocking {
        val user = newSuspendedTransaction {
            userService.getUser(newUser.email).toDto()
        }

        val userFromBd = newSuspendedTransaction {
            User.find {
                UsersTable.email eq newUser.email
            }.first().toDto()
        }
        println(userFromBd)
        assertEquals(userFromBd, user)

    }

    @Test
    fun clear(){
        transaction {
            UsersTable.deleteAll()
        }
    }
}