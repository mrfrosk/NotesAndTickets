package com.example.first

import com.example.first.Services.UserService
import com.example.first.Services.utils.Hashing
import com.example.first.database.dto.UserFullDto
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class ProfileServiceTest {

    @Autowired
    lateinit var userService: UserService
    private final val mail = "testMail.ru"
    private final val mail2 = "testMail2.ru"
    val userFullDto = UserFullDto(mail, "testName", "testSurname", "testPatronymic", "testPassword")
    val userFullDto2 = UserFullDto(mail2, "testName1", "testSurname1", "testPatronymic1", "testPassword1")

    @BeforeEach
    fun initData() {
        transaction {
            User.new {
                this.email = userFullDto.email
                this.name = userFullDto.name
                this.surname = userFullDto.surname
                this.patronymic = userFullDto.patronymic
                this.password = userFullDto.password
            }
        }
    }

    @Test
    fun getUserSuccessful() {
        val user = transaction {
            userService.getUser(userFullDto.email).toFullDto()
        }
        assertEquals(userFullDto, user)
    }

    @Test
    fun getUserUnsuccessful() {
        assertThrows<IllegalArgumentException> {
            transaction {
                userService.getUser("asdasdasdas")
            }
        }
    }

    @Test
    fun updateUser() {
        val user = transaction {
            userService.updateUser(mail, userFullDto2)
            userService.getUser(mail2)
        }
        userFullDto2.password = Hashing.toSha256(userFullDto2.password)
        assertEquals(userFullDto2, user.toFullDto())
    }

    @BeforeEach
    fun clearData() {
        transaction {
            UsersTable.deleteAll()
        }
    }
}