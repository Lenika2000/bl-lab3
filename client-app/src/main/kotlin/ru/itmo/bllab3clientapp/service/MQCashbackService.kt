package ru.itmo.bllab3clientapp.service

import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.core.JmsTemplate
import ru.itmo.bllab3clientapp.model.CashbackStatus
import ru.itmo.bllab3clientapp.repo.CashbackRepository
import ru.itmo.bllab3clientapp.repo.ClientRepository
import javax.persistence.EntityNotFoundException

data class CashbackChangeStatusRequest (
        val cashbackId: Long,
        val status: CashbackStatus,
        val cashbackSum: Double
)

class MQCashbackService(
        private val clientRepository: ClientRepository,
        private val cashbackRepository: CashbackRepository,
        private val messageService: MessageService,
        private val jmsTemplate: JmsTemplate
) {
    @JmsListener(destination = "ChangeCashbackStatusQueue")
    fun receiveCashbackStatus(cashbackChangeStatusRequest: CashbackChangeStatusRequest) {
        val cashbackId = cashbackChangeStatusRequest.cashbackId;
        val cashback = cashbackRepository.findById(cashbackId).orElseThrow {
            EntityNotFoundException("Кэшбек с id $cashbackId не найден!")
        }
        if (cashbackChangeStatusRequest.status == CashbackStatus.APPROVED) {
            cashback.cashbackSum = cashbackChangeStatusRequest.cashbackSum
            cashback.client.balance += cashback.cashbackSum * 0.9
            clientRepository.save(cashback.client)
            messageService.sendNotificationToClient("Кэшбек выплачен", cashback.client)
            cashback.status = CashbackStatus.RECEIVED_SUM
            jmsTemplate.convertAndSend("CashbackStatusReceivedSumQueue", cashback.id)
        } else {
            cashback.status = CashbackStatus.REJECTED
        }
        cashbackRepository.save(cashback)
    }
}
