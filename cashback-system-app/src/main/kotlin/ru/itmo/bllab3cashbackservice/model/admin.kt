package ru.itmo.bllab3cashbackservice.model

import javax.persistence.*

@Entity
class Admin(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @Column(name = "first_name")
        var firstName: String = "",
        @Column(name = "last_name")
        var lastName: String = "",
        @OneToOne(mappedBy = "admin")
        var eUser: EUser = EUser()
)
