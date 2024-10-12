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
    val newUserDto = NewUserDto(mail, "testName", "testSurname", "testPatronymic", "testPassword")
    val newUserDto2 = NewUserDto(mail2, "testName1", "testSurname1", "testPatronymic1", "testPassword1")

    @BeforeEach
    fun initData() {
        transaction {
            User.new {
                this.email = newUserDto.email
                this.name = newUserDto.name
                this.surname = newUserDto.surname
                this.patronymic = newUserDto.patronymic
                this.password = newUserDto.password
            }
        }
    }

    @Test
    fun getUserSuccessful() {
        runBlocking {
            val user = newSuspendedTransaction {
                userService.getUser(newUserDto.email).toNewDto()
            }
            assertEquals(newUserDto, user)
        }
    }

    @Test
    fun getUserUnsuccessful() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                newSuspendedTransaction {
                    userService.getUser("asdasdasdas")
                }
            }
        }
    }

    @Test
    fun updateUser() {
        runBlocking { val user = newSuspendedTransaction {
            userService.updateUser(mail, newUserDto2)
            userService.getUser(mail2)
        }
            newUserDto2.password = Hashing.toSha256(newUserDto2.password)
            assertEquals(newUserDto2, user.toNewDto()) }
    }

    @BeforeEach
    fun clearData() {
        transaction {
            UsersTable.deleteAll()
        }
    }
}