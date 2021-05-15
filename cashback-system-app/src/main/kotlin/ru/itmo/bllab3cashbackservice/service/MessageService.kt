package ru.itmo.bllab3cashbackservice.service

import org.springframework.stereotype.Service
import ru.itmo.bllab3cashbackservice.model.Client

interface MessageService {
    fun sendNotificationToClient(notification: String, client: Client)
}

@Service
class MessageServiceStub : MessageService {
    override fun sendNotificationToClient(notification: String, client: Client) {}
}
