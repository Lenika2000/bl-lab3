package ru.itmo.bllab3cashbackservice.service

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.itmo.bllab3cashbackservice.model.Admin
import ru.itmo.bllab3cashbackservice.model.ERole
import ru.itmo.bllab3cashbackservice.model.EUser
import ru.itmo.bllab3cashbackservice.model.Role
import ru.itmo.bllab3cashbackservice.repo.AdminRepository
import ru.itmo.bllab3cashbackservice.repo.RoleRepository
import ru.itmo.bllab3cashbackservice.repo.UserRepository

@Service
class PopulateDBService(
        private val roleRepository: RoleRepository,
        private val adminRepository: AdminRepository,
        private val userRepository: UserRepository,
        private val encoder: PasswordEncoder,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        val roleClient = Role(name = ERole.ROLE_CLIENT)
        val roleShop = Role(name = ERole.ROLE_SHOP)
        val roleAdmin = Role(name = ERole.ROLE_ADMIN)
        roleRepository.save(roleAdmin)
//        roleRepository.save(roleClient)
        roleRepository.save(roleShop)
//        val adm = Admin(0, "Маньшина", "Елена");
//        val admin = EUser(0, "admin", encoder.encode("666666"), null,  adm, setOf(roleAdmin))
//        adm.eUser = admin
//        userRepository.save(admin)
//        adminRepository.save(adm)
    }
}
