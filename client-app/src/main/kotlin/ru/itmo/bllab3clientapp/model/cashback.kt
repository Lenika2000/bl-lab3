package ru.itmo.bllab3clientapp.model

import java.time.LocalDateTime
import javax.persistence.*

enum class CashbackStatus {
    NEW,
    RECEIVED_INF,
    APPROVED,
    REJECTED,
    RECEIVED_SUM
}

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
        @Column(name = "is_paid")
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
