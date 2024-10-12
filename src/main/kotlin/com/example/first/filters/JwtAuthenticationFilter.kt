package com.example.first.filters


import com.example.first.Services.AuthService
import com.example.first.Services.JwtService
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
    lateinit var authService: AuthService

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
        if (!tokenService.verifyAccessToken(jwtToken)) {
            response.setHeader("Not-Verified", "0")
            filterChain.doFilter(request, response)
            return
        }

        val email = tokenService.getEmail(jwtToken)

        if (authService.isExistsByEmail(email)) {
            updateContext(email, request)
            filterChain.doFilter(request, response)
        }
    }

    fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    fun String.extractTokenValue() =
        this.substringAfter("Bearer ")

    fun updateContext(email: String, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(email, null, null)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

}