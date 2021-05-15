package ru.itmo.bllab3clientapp.controller

import org.springframework.http.ResponseEntity
import org.springframework.jms.core.JmsTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import ru.itmo.bllab3clientapp.auth.JwtUtils
import ru.itmo.bllab3clientapp.auth.UserDetailsImpl
import ru.itmo.bllab3clientapp.model.*
import ru.itmo.bllab3clientapp.repo.*
import java.util.stream.Collectors
import javax.persistence.EntityNotFoundException

data class JwtResponse(
        val login: String,
        val roles: Collection<String>,
        val accessToken: String,
)

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api")
@RestController
class UserController(
        private val authenticationManager: AuthenticationManager,
        private val jwtUtils: JwtUtils,
        private val encoder: PasswordEncoder,
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val clientRepository: ClientRepository,
        private val jmsTemplate: JmsTemplate
) {

    companion object {
        fun mapClientData(client: Client): ClientData =
                ClientData(client.id, client.firstName, client.lastName, client.balance)
    }

    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<*>? {
        val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.login, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val user = userRepository.findByLogin(userDetails.username)
                .orElseThrow { EntityNotFoundException("Пользователь не найден") }
        return ResponseEntity.ok(JwtResponse(
                user.login,
                userDetails.authorities.stream()
                        .map { v -> v.authority }
                        .collect(Collectors.toList()),
                jwt,
        ))
    }

    @PostMapping("/client/register")
    fun registerClient(@RequestBody payload: RegisterUserRequest): MessageIdResponse {
        if (userRepository.findByLogin(payload.login).isPresent)
            throw IllegalStateException("Пользователь с таким логином уже зарегистрирован")
        val client = Client(0, payload.firstName, payload.lastName)
        val user = EUser(
                0, payload.login, encoder.encode(payload.password), client,
                setOf(roleRepository.findRoleByName(ERole.ROLE_CLIENT).get())
        )
        client.eUser = user
        userRepository.save(user)
        clientRepository.save(client)
        jmsTemplate.convertAndSend("NewClientsQueue", ClientDataForService(client.id,
                client.firstName, client.lastName))
        return MessageIdResponse("Регистрация клиента прошла успешно", client.id)
    }
}

data class ClientDataForService(
        val id: Long,
        val firstName: String,
        val lastName: String,
)
