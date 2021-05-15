package ru.itmo.bllab3clientapp.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.itmo.bllab3clientapp.auth.UserDetailsImpl
import ru.itmo.bllab3clientapp.model.ERole
import ru.itmo.bllab3clientapp.model.EUser
import ru.itmo.bllab3clientapp.repo.UserRepository


@Service
class UserService(
        private val userRepository: UserRepository,
) {
    fun getCurrentUserId(): Long = (SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl).id

    fun getUserFromAuth(): EUser = userRepository.findById(getCurrentUserId())
            .orElseThrow { UsernameNotFoundException("User not found - ${getCurrentUserId()}") }

    fun checkClientAuthority(clientId: Long) {
        val accessor = getUserFromAuth()
        if (accessor.roles.any { r -> r.name == ERole.ROLE_ADMIN })
            return
        val client = accessor.client
        if (clientId != client?.id)
            throw IllegalAccessException("Доступ запрещен")
    }

}
