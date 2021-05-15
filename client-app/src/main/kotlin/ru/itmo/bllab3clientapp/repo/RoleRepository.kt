package ru.itmo.bllab3clientapp.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3clientapp.model.ERole
import ru.itmo.bllab3clientapp.model.Role
import java.util.*

interface RoleRepository : CrudRepository<Role, Long> {
    fun findRoleByName(name: ERole): Optional<Role>
}
