package ru.itmo.bllab3cashbackservice.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3cashbackservice.model.EUser
import java.util.*

interface UserRepository : CrudRepository<EUser, Long> {
    fun findByLogin(login: String): Optional<EUser>
}
