package ru.itmo.bllab3clientapp.model
import javax.persistence.*

@Entity
class Shop(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "",
)
