package com.example.first.Services

import com.example.first.Services.dto.AuthDto
import com.example.first.Services.utils.Hashing
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuthService {
    fun authUser(authDto: AuthDto): Boolean {
        val authSuccessful = transaction {
            User.find {
                (UsersTable.email eq authDto.email) and
                        (UsersTable.password eq Hashing.toSha256(authDto.password))
            }.firstOrNull()
        }
        return authSuccessful != null
    }
}