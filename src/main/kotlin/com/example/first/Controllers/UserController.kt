package com.example.first.Controllers

import com.example.first.Services.UserService
import com.example.first.database.dto.AuthDto
import com.example.first.database.dto.UserFullDto
import com.example.first.database.dto.UserInfoDto
import com.example.first.database.entities.User
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @PostMapping("reg-user")
    fun createUser(@RequestBody user: String): Boolean {
        val userFullDto = Json.decodeFromString<UserFullDto>(user)
        userService.createUser(userFullDto)
        return true
    }

    @GetMapping("get-user/{email}")
    fun getUser(@PathVariable("email") email: String): User {
        return userService.getUser(email)
    }

    @GetMapping
    fun getUsers(): List<UserInfoDto> {
        return userService.getUsers()
    }

    @PostMapping("login")
    fun authUser(@RequestBody authBody: String): Boolean {
        val userInfo = Json.decodeFromString<AuthDto>(authBody)
        return userService.authUser(userInfo.email, userInfo.password)
    }

}