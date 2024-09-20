package com.example.first

import com.example.first.Services.utils.MailSender
import com.example.first.configuration.yaml.SmtpProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FirstApplicationTests {

	@Autowired
	lateinit var sender: MailSender
	@Test
	fun send(){
		sender.send("heroker44@gmail.com", "Можно релизить", "Оповещение")
	}

}
