package ru.itmo.bllab3messages


import java.io.Serializable

data class CashbackChangeStatusRequest (
        val cashbackId: Long,
        val status: CashbackStatus,
        val cashbackSum: Double
): Serializable
