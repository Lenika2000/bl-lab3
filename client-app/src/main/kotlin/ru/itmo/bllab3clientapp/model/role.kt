package ru.itmo.bllab3clientapp.model
import javax.persistence.*

@Entity
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Enumerated(EnumType.STRING)
        val name: ERole = ERole.ROLE_CLIENT,
)

enum class ERole {
    ROLE_CLIENT,
    ROLE_ADMIN
}
