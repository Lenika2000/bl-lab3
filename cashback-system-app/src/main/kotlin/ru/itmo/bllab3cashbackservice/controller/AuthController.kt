package ru.itmo.bllab3cashbackservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import ru.itmo.bllab3cashbackservice.auth.JwtUtils
import ru.itmo.bllab3cashbackservice.auth.UserDetailsImpl
import ru.itmo.bllab3cashbackservice.model.*
import ru.itmo.bllab3cashbackservice.repo.*
import ru.itmo.bllab3cashbackservice.service.UserService
import java.util.stream.Collectors
import javax.persistence.EntityNotFoundException

data class JwtResponse(
        val login: String,
        val roles: Collection<String>,
        val accessToken: String,
)

data class MessageIdResponse(
        val message: String,
        val id: Long? = null,
)

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api")
@RestController
class UserController(
        private val authenticationManager: AuthenticationManager,
        private val jwtUtils: JwtUtils,
        private val encoder: PasswordEncoder,
        private val userService: UserService,
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val shopRepository: ShopRepository,
        private val adminRepository: AdminRepository
) {

    companion object {
        fun mapShopData(shop: Shop): ShopData =
                ShopData(shop.id, shop.name)
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

    @GetMapping("/shop/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP')")
    fun getShopData(@PathVariable id: Long): ShopData {
        userService.checkShopAuthority(id)
        val shop = shopRepository.findById(id).orElseThrow {
            EntityNotFoundException("Магазин с id $id не найден!")
        }
        return mapShopData(shop)
    }

    @GetMapping("/shops")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getShopsData(): Iterable<ShopData> = shopRepository.findAll()
            .map { shop: Shop -> mapShopData(shop) }

    @PostMapping("/shop/register")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun registerShop(@RequestBody payload: RegisterShopRequest): MessageIdResponse {
        if (userRepository.findByLogin(payload.login).isPresent)
            throw IllegalStateException("Пользователь с таким логином уже зарегистрирован")
        val shop = Shop(0, payload.name)
        val user = EUser(
                0, payload.login, encoder.encode(payload.password), null,  null,
                setOf(roleRepository.findRoleByName(ERole.ROLE_SHOP).get())
        )
        shop.eUser = user
        userRepository.save(user)
        shopRepository.save(shop)
        return MessageIdResponse("Регистрация магазина прошла успешно", shop.id)
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun registerAdmin(@RequestBody payload: RegisterUserRequest): MessageIdResponse {
        if (userRepository.findByLogin(payload.login).isPresent)
            throw IllegalStateException("Пользователь с таким логином уже зарегистрирован")
        val admin = Admin(0, payload.firstName, payload.lastName)
        val user = EUser(
                0, payload.login, encoder.encode(payload.password), null, admin,
                setOf(roleRepository.findRoleByName(ERole.ROLE_ADMIN).get())
        )
        admin.eUser = user
        userRepository.save(user)
        adminRepository.save(admin)
        return MessageIdResponse("Регистрация администратора прошла успешно", admin.id)
    }
}
