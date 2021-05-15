package ru.itmo.bllab3cashbackservice.model
import javax.persistence.*

@Entity
class EUser(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var login: String = "",
        var password: String = "",
        @OneToOne(cascade = [CascadeType.ALL])
        var shop: Shop? = null,
        @OneToOne(cascade = [CascadeType.ALL])
        var admin: Admin? = null,
        @ManyToMany(fetch = FetchType.EAGER)
        var roles: Set<Role> = emptySet(),
)

data class RegisterUserRequest(
        val login: String,
        val password: String,
        val firstName: String,
        val lastName: String
)

data class LoginRequest(
        val login: String,
        val password: String
)
