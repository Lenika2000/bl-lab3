package ru.itmo.bllab3cashbackservice.mq

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import ru.itmo.bllab3cashbackservice.repo.CashbackRepository
import ru.itmo.bllab3messages.CashbackStatus
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.ObjectMessage
import javax.persistence.EntityNotFoundException

@Service
class MQCashbackStatusReceivedSum (
        private val cashbackRepository: CashbackRepository
): MessageListener {

    @JmsListener(destination = "CashbackStatusReceivedSumQueue")
    override fun onMessage(message: Message?) {
        try {
            val objectMessage: ObjectMessage = message as ObjectMessage
            val cashbackId: Long = objectMessage.getObject() as Long
            val cashback = cashbackRepository.findById(cashbackId).orElseThrow {
                EntityNotFoundException("Кэшбек с id ${cashbackId} не найден!")
            }
            cashback.status = CashbackStatus.RECEIVED_SUM
            cashbackRepository.save(cashback)
        } catch (e: Exception) {
            System.out.println(e)
        }
    }
}
