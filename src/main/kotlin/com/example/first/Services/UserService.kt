package com.example.first.Services

import com.example.first.Services.utils.Hashing
import com.example.first.database.dto.UserFullDto
import com.example.first.database.dto.UserInfoDto
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class UserService {

    fun createUser(userFullDto: UserFullDto) {
        transaction {
            val conditions = userCondition(
                userFullDto.email,
                userFullDto.name,
                userFullDto.surname,
                userFullDto.patronymic
            )
            val user = User.find { conditions.reduce { acc, op -> acc and op } }.firstOrNull()
            require(user == null) { " Данный пользователь уже существует " }
            User.new {
                email = userFullDto.email
                name = userFullDto.name
                surname = userFullDto.surname
                patronymic = userFullDto.patronymic
                password = Hashing.toSha256(userFullDto.password)
            }
        }
    }

    fun getUsers(): List<UserInfoDto> {
        return transaction {
            User.all().map { it.toInfoDto() }
        }
    }

    fun getUser(email: String): User {
        val user = transaction {
            User.find { UsersTable.email eq email }.firstOrNull()
        }
        require(user != null) { "Пользователя с электронной почтой $email не существует " }
        return user
    }

    fun isExistsByEmail(email: String): Boolean {
        return transaction {
            User.find {
                UsersTable.email eq email
            }.firstOrNull()
        } != null
    }

    fun updateUser(email: String, userFullDto: UserFullDto) {
        val user = getUser(email)
        user.email = userFullDto.email
        user.name = userFullDto.name
        user.surname = userFullDto.surname
        user.patronymic = userFullDto.patronymic
        user.password = Hashing.toSha256(userFullDto.password)
    }

    private fun userCondition(
        email: String? = null,
        name: String? = null,
        surname: String? = null,
        patronymic: String? = null
    ): List<Op<Boolean>> {
        return listOfNotNull(
            email?.let { UsersTable.email eq email },
            name?.let { UsersTable.name eq name },
            surname?.let { UsersTable.surname eq surname },
            patronymic?.let { UsersTable.patronymic eq patronymic }
        )
    }
}

