package ru.itmo.bllab3cashbackservice.auth

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.*


@Component
class JwtUtils {

    private val jwtSecret: String = "test"

    private val jwtExpirationMs = 86400000

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        return Jwts.builder()
                .setSubject(userPrincipal.username)
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String?): String? {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            true
        } catch (e: Exception) {
            false
        }
    }
}
