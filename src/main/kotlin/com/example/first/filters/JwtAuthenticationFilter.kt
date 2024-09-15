package com.example.first.filters


import com.example.first.Services.JwtService
import com.example.first.Services.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter: OncePerRequestFilter() {

    @Autowired
    lateinit var tokenService: JwtService
    @Autowired
    lateinit var userService: UserService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")

        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader!!.extractTokenValue()
        val email = tokenService.getEmail(jwtToken)

        if (userService.isExistsByEmail(email)) {
            updateContext(email, request)
        }
        filterChain.doFilter(request, response)
    }

    fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    fun String.extractTokenValue() =
        this.substringAfter("Bearer ")

    fun updateContext(email: String, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(email, null)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

}