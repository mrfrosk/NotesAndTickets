package com.example.first.database.entities

import com.example.first.database.dto.UserInfoDto
import com.example.first.database.dto.UserFullDto
import com.example.first.database.tables.UsersTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

data class User(val _id: EntityID<UUID>) : Entity<UUID>(_id) {
    companion object : EntityClass<UUID,User>(UsersTable)

    var email by UsersTable.email
    var name by UsersTable.name
    var surname by UsersTable.surname
    var patronymic by UsersTable.patronymic
    var password by UsersTable.password

    fun toFullDto() = UserFullDto(email, name, surname, patronymic, password)

    fun toInfoDto() = UserInfoDto(name, surname, patronymic, email, id.value)
}