package ru.itmo.bllab3messages

import java.io.Serializable

data class ClientDataForService (
        val id: Long,
        val firstName: String,
        val lastName: String,
): Serializable
