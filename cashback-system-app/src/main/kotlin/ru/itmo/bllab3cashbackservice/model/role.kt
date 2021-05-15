package ru.itmo.bllab3cashbackservice.model
import javax.persistence.*

@Entity
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Enumerated(EnumType.STRING)
        val name: ERole = ERole.ROLE_SHOP,
)

enum class ERole {
    ROLE_SHOP,
    ROLE_ADMIN
}
