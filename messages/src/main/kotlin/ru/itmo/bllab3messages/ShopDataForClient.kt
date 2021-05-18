package ru.itmo.bllab3messages

import java.io.Serializable

data class ShopDataForClient (
        val id: Long,
        val name: String,
): Serializable
