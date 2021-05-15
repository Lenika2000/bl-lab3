package ru.itmo.bllab3clientapp.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3clientapp.model.EUser
import java.util.*

interface UserRepository : CrudRepository<EUser, Long> {
    fun findByLogin(login: String): Optional<EUser>
}
