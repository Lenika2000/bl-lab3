package ru.itmo.bllab3cashbackservice.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3cashbackservice.model.ERole
import ru.itmo.bllab3cashbackservice.model.Role
import java.util.*

interface RoleRepository : CrudRepository<Role, Long> {
    fun findRoleByName(name: ERole): Optional<Role>
}
