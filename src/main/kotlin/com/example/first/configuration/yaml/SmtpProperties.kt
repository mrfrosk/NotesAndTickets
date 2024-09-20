package com.example.first.configuration.yaml

import com.example.first.configuration.yaml.dto.SmtpDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SmtpProperties {
    @Autowired
    @Value("\${mail.server-conf.address}")
    private lateinit var address: String

    @Autowired
    @Value("\${mail.server-conf.port}")
    private lateinit var port: String

    @Autowired
    @Value("\${mail.user-conf.username}")
    private lateinit var username: String

    @Autowired
    @Value("\${mail.user-conf.password}")
    private lateinit var password: String

    fun toDto() = SmtpDto(address, port.toInt(), username, password)
}