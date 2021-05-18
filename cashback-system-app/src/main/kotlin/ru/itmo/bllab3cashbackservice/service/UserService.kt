package ru.itmo.bllab3cashbackservice.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.itmo.bllab3cashbackservice.auth.UserDetailsImpl
import ru.itmo.bllab3cashbackservice.model.ERole
import ru.itmo.bllab3cashbackservice.model.EUser
import ru.itmo.bllab3cashbackservice.repo.CashbackRepository
import ru.itmo.bllab3cashbackservice.repo.UserRepository


@Service
class UserService(
        private val userRepository: UserRepository,
) {
    fun getCurrentUserId(): Long = (SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl).id

    fun getUserFromAuth(): EUser = userRepository.findById(getCurrentUserId())
            .orElseThrow { UsernameNotFoundException("User not found - ${getCurrentUserId()}") }

    fun checkShopAuthority(shopId: Long) {
        val accessor = getUserFromAuth()
        if (accessor.roles.any { r -> r.name == ERole.ROLE_ADMIN })
            return
        val shop = accessor.shop
        if (shopId != shop?.id)
            throw IllegalAccessException("Доступ запрещен")
    }

}
