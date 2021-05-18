package ru.itmo.bllab3cashbackservice.mq

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import ru.itmo.bllab3cashbackservice.model.Cashback
import ru.itmo.bllab3cashbackservice.repo.CashbackRepository
import ru.itmo.bllab3cashbackservice.repo.ClientRepository
import ru.itmo.bllab3cashbackservice.repo.ShopRepository
import ru.itmo.bllab3messages.CashbackDataForService
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.ObjectMessage
import javax.persistence.EntityNotFoundException

@Service
class MQCreateCashback (
        private val cashbackRepository: CashbackRepository,
        private val clientRepository: ClientRepository,
        private val shopRepository: ShopRepository
): MessageListener {

    @JmsListener(destination = "CreateCashbackQueue")
    override fun onMessage(message: Message?) {
        try {
            val objectMessage: ObjectMessage = message as ObjectMessage
            val newCashback: CashbackDataForService = objectMessage.getObject() as CashbackDataForService
            val client = clientRepository.findById(newCashback.clientId).orElseThrow {
                EntityNotFoundException("Кэшбек с id ${newCashback.clientId} не найден!")
            }
            val shop = shopRepository.findById(newCashback.shopId).orElseThrow {
                EntityNotFoundException("Кэшбек с id ${newCashback.shopId} не найден!")
            }
            cashbackRepository.save(Cashback(newCashback.id, newCashback.startDate, client, shop))
        } catch (e: Exception) {
            System.out.println(e)
        }
    }
}
