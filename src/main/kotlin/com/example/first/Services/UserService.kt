package com.example.first.Services

import com.example.first.Services.utils.Hashing
import com.example.first.Services.dto.NewUserDto
import com.example.first.database.dto.UserDto
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Service

@Service
class UserService {

    suspend fun createUser(newUserDto: NewUserDto): UserDto {

        val conditions = userCondition(
            newUserDto.email,
            newUserDto.name,
            newUserDto.surname,
            newUserDto.patronymic
        )
        val user = User.find { conditions.reduce { acc, op -> acc and op } }.firstOrNull()
        require(user == null) { " Данный пользователь уже существует " }
        return User.new {
            email = newUserDto.email
            name = newUserDto.name
            surname = newUserDto.surname
            patronymic = newUserDto.patronymic
            password = Hashing.toSha256(newUserDto.password)
        }.toDto()
    }

    suspend fun getUsers(): List<UserDto> {
        return User.all().map { it.toDto() }
    }

    suspend fun getUser(email: String): User {
        val user =
            User.find { UsersTable.email eq email }.firstOrNull()

        require(user != null) { "Пользователя с электронной почтой $email не существует " }
        return user
    }

    suspend fun updateUser(email: String, newUserDto: NewUserDto) {
        val user = getUser(email)
        user.email = newUserDto.email
        user.name = newUserDto.name
        user.surname = newUserDto.surname
        user.patronymic = newUserDto.patronymic
        user.password = Hashing.toSha256(newUserDto.password)
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

