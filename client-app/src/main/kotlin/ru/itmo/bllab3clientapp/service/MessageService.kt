package ru.itmo.bllab3clientapp.service



import org.springframework.stereotype.Service
import ru.itmo.bllab3clientapp.model.Client

interface MessageService {
    fun sendNotificationToClient(notification: String, client: Client)
}

@Service
class MessageServiceStub : MessageService {
    override fun sendNotificationToClient(notification: String, client: Client) {}
}
