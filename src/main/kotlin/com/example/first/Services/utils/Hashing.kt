package com.example.first.Services.utils

import java.security.MessageDigest

object Hashing {
    fun toSha256(text: String): String{
        val sha = MessageDigest.getInstance("SHA-256")
        return sha.digest(text.toByteArray()).fold("") { str, it -> str + "%02x".format(it) }
    }
}