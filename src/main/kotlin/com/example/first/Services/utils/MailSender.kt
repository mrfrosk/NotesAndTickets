package com.example.first.Services.utils


import com.example.first.configuration.yaml.SmtpProperties
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MailSender {
    @Autowired
    lateinit var smtpProperties: SmtpProperties

    fun send(emailTo: String, message: String, subject: String=""){
        val smtpDto = smtpProperties.toDto()
        val email = EmailBuilder.startingBlank()
            .from(smtpDto.username)
            .to(emailTo)
            .withPlainText(message)
            .withSubject(subject)
            .buildEmail()

        val mailer = MailerBuilder
            .withSMTPServer(smtpDto.address, smtpDto.port, smtpDto.username, smtpDto.password)
            .buildMailer()
        mailer.sendMail(email)
    }
}