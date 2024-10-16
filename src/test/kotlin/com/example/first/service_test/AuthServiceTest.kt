package com.example.first.service_test

import com.example.first.Services.AuthService
import com.example.first.Services.dto.AuthDto
import com.example.first.Services.enums.RequestStatus
import com.example.first.Services.utils.Hashing
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
class AuthServiceTest {
    @Autowired
    lateinit var authService: AuthService
    val userEmail = "test@mail.ru"
    val userPassword = "123"

    @BeforeEach
    fun init(): Unit = transaction {
        clear()
        User.new {
            email = userEmail
            name = "test"
            surname = "test"
            patronymic = "test"
            password = Hashing.toSha256(userPassword)
        }
    }

    @Test
    fun isExistEmail(): Unit = transaction {
        val isExist = authService.isExistsByEmail(userEmail)
        val isNotExists = authService.isExistsByEmail("a")

        assertEquals(true, isExist)
        assertEquals(false, isNotExists)
    }

    @Test
    fun login(): Unit = transaction {
        val successAuthDto = AuthDto(userEmail, userPassword)
        val deniedAuthDto = AuthDto(userEmail, "12")
        val successLogin = authService.login(successAuthDto)
        val deniedLogin = authService.login(deniedAuthDto)
        assertEquals(RequestStatus.Success, successLogin)
        assertEquals(RequestStatus.Denied, deniedLogin)
    }

    @AfterEach
    fun clear(): Unit = transaction {
        UsersTable.deleteAll()
    }
}