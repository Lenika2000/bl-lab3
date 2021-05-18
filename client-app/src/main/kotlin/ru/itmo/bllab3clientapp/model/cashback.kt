package ru.itmo.bllab3clientapp.model

import ru.itmo.bllab3messages.CashbackStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Cashback(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var startDate: LocalDateTime = LocalDateTime.now(),
        @ManyToOne
        var client: Client = Client(),
        @ManyToOne
        var shop: Shop = Shop(),
        var status: CashbackStatus = CashbackStatus.NEW,
        @Column(name = "cashback_sum")
        var cashbackSum: Double = 0.0
)


data class CashbackData (
        val id: Long,
        val startDate: LocalDateTime,
        val clientFirstName: String,
        val clientLastName: String,
        val shopName: String,
        var status: CashbackStatus,
        var cashbackSum: Double,
)

data class CashbackDataForClient (
        val id: Long,
        val startDate: LocalDateTime,
        val shopName: String,
        var status: CashbackStatus,
        var cashbackSum: Double,
)
