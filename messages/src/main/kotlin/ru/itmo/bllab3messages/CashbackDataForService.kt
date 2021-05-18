package ru.itmo.bllab3messages

import java.io.Serializable
import java.time.LocalDateTime

data class CashbackDataForService (
        val id: Long,
        val startDate: LocalDateTime,
        val clientId: Long,
        val shopId: Long
): Serializable
