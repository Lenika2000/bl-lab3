package ru.itmo.bllab3clientapp.mq

import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import ru.itmo.bllab3clientapp.repo.CashbackRepository
import ru.itmo.bllab3clientapp.repo.ClientRepository
import ru.itmo.bllab3messages.CashbackChangeStatusRequest
import ru.itmo.bllab3messages.CashbackStatus
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.ObjectMessage
import javax.persistence.EntityNotFoundException

@Service
class MQChangeCashbackStatus (
        private val cashbackRepository: CashbackRepository,
        private val clientRepository: ClientRepository,
        private val jmsTemplate: JmsTemplate
): MessageListener {

    @JmsListener(destination = "ChangeCashbackStatusQueue")
    override fun onMessage(message: Message?) {
        try {
            val objectMessage: ObjectMessage = message as ObjectMessage
            val changedCashback: CashbackChangeStatusRequest = objectMessage.getObject() as CashbackChangeStatusRequest
            val cashback = cashbackRepository.findById(changedCashback.cashbackId).orElseThrow {
                EntityNotFoundException("Кэшбек с id $changedCashback.cashbackId не найден!")
            }
            if (changedCashback.status == CashbackStatus.APPROVED) {
                cashback.cashbackSum = changedCashback.cashbackSum
                cashback.client.balance += cashback.cashbackSum * 0.9
                clientRepository.save(cashback.client)
                cashback.status = CashbackStatus.RECEIVED_SUM
                jmsTemplate.convertAndSend("CashbackStatusReceivedSumQueue", cashback.id)
            } else {
                cashback.status = CashbackStatus.REJECTED
            }
            cashbackRepository.save(cashback)
        } catch (e: Exception) {
            System.out.println(e)
        }
    }
}
