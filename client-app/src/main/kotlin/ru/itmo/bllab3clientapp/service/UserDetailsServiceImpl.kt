package ru.itmo.bllab3clientapp.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.itmo.bllab3clientapp.auth.UserDetailsImpl
import ru.itmo.bllab3clientapp.repo.UserRepository

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
            UserDetailsImpl.build(userRepository.findByLogin(username)
                    .orElseThrow { UsernameNotFoundException("Имя пользователя не найдено - $username") })
}
