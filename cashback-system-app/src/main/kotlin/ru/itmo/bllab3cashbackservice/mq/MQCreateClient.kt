package ru.itmo.bllab3cashbackservice.mq

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import ru.itmo.bllab3cashbackservice.model.Client
import ru.itmo.bllab3cashbackservice.repo.ClientRepository
import ru.itmo.bllab3messages.ClientDataForService
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.ObjectMessage


@Service
class MQCreateClient(
        private val clientRepository: ClientRepository
): MessageListener {
    @JmsListener(destination = "NewClientsQueue")
    override fun onMessage(message: Message?) {
        try {
            val objectMessage: ObjectMessage = message as ObjectMessage
            val client: ClientDataForService = objectMessage.getObject() as ClientDataForService
            clientRepository.save(Client(client.id, client.firstName, client.lastName))
        } catch (e: Exception) {
            System.out.println(e)
        }
    }
}
