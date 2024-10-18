package com.example.first.Controllers

import com.example.first.Services.UserService
import com.example.first.database.dto.NewUserDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Mapping.USERS)
class UserController {
    @Autowired
    lateinit var userService: UserService

    @PostMapping("/new")
    suspend fun createUser(@RequestBody user: String): ResponseEntity<*> {
        val newUserDto = Json.decodeFromString<NewUserDto>(user)
        val newUser = newSuspendedTransaction {
            userService.createUser(newUserDto)
        }
        return ResponseEntity.ok().body(Json.encodeToString(newUser))
    }

    @GetMapping("/user/{email}")
    suspend fun getUser(@PathVariable("email") email: String): ResponseEntity<*> {
        val user = newSuspendedTransaction {
            userService.getUser(email).toDto()
        }
        return ResponseEntity.ok().body(user)
    }

    @GetMapping("/all")
    suspend fun getUsers(): ResponseEntity<*> {
        val users = newSuspendedTransaction {
            userService.getUsers()
        }
        return ResponseEntity.ok().body(users)
    }


}