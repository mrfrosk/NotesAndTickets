package com.example.first.configuration

import com.example.first.Controllers.Mapping
import com.example.first.filters.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Autowired
    lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): DefaultSecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.POST, "${Mapping.USERS}/new")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "${Mapping.AUTH}/login")
                    .permitAll()
                    .requestMatchers("${Mapping.NOTES}/**")
                    .permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}