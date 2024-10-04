package com.example.first.Services

import com.example.first.Services.dto.AuthDto
import com.example.first.Services.enums.LoginStatus
import com.example.first.Services.utils.Hashing
import com.example.first.database.entities.User
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuthService {
    fun login(authDto: AuthDto): LoginStatus {
        val authSuccessful = transaction {
            User.find {
                (UsersTable.email eq authDto.email) and
                        (UsersTable.password eq Hashing.toSha256(authDto.password))
            }.firstOrNull()
        }
        return if (authSuccessful != null){
            LoginStatus.Success
        } else{
            LoginStatus.Denied
        }
    }

    fun isExistsByEmail(email: String): Boolean {
        return transaction {
            User.find {
                UsersTable.email eq email
            }.firstOrNull()
        } != null
    }
}