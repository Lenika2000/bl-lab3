package ru.itmo.bllab3clientapp.service

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.itmo.bllab3clientapp.model.ERole
import ru.itmo.bllab3clientapp.model.EUser
import ru.itmo.bllab3clientapp.model.Role
import ru.itmo.bllab3clientapp.repo.RoleRepository
import ru.itmo.bllab3clientapp.repo.UserRepository

@Service
class PopulateDBService(
        private val roleRepository: RoleRepository,
        private val userRepository: UserRepository,
        private val encoder: PasswordEncoder,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val roleClient = Role(name = ERole.ROLE_CLIENT)
//        val roleShop = Role(name = ERole.ROLE_SHOP)
//        val roleAdmin = Role(name = ERole.ROLE_ADMIN)
//        roleRepository.save(roleAdmin)
        roleRepository.save(roleClient)
//        roleRepository.save(roleShop)
//        val client = Client(0, "Маньшина", "Елена");
//        val client = EUser(0, "lena", encoder.encode("666666"), null, null, adm, setOf(roleAdmin))
//        adm.eUser = admin
//        userRepository.save(admin)
//        adminRepository.save(adm)
    }
}
