package ru.itmo.bllab3cashbackservice.service

import org.springframework.jms.annotation.JmsListener
import ru.itmo.bllab3cashbackservice.model.Client
import ru.itmo.bllab3cashbackservice.repo.ClientRepository

class MQClientService(
        private val clientRepository: ClientRepository
) {
    @JmsListener(destination = "NewClientsQueue")
    fun receiveNewClient(client: Client) {
        clientRepository.save(client);
    }
}
