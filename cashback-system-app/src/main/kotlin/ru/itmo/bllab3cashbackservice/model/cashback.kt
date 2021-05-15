package ru.itmo.bllab3cashbackservice.model

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
        var isPaid: Boolean = false,
        @Column(name = "is_order_completed")
        var isOrderCompleted: Boolean = false,
        @Column(name = "confirm_payment")
        var confirmPayment: Boolean = false,
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
        var isPaid: Boolean,
        var isOrderCompleted: Boolean,
        var confirmPayment: Boolean,
        var status: CashbackStatus,
        var cashbackSum: Double,
)

data class CashbackDataForShop (
        val id: Long,
        val startDate: LocalDateTime,
        val clientFirstName: String,
        val clientLastName: String,
        var isPaid: Boolean,
        var isOrderCompleted: Boolean,
        var confirmPayment: Boolean,
        var status: CashbackStatus,
        var cashbackSum: Double,
)
