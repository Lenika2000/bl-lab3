package ru.itmo.bllab3cashbackservice.model
import javax.persistence.*

@Entity
class Shop(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "",
        @OneToOne(mappedBy = "shop")
        var eUser: EUser = EUser()
)

data class ShopData(
        val id: Long,
        val name: String,
)

data class RegisterShopRequest(
        val login: String,
        val password: String,
        val name: String
)
