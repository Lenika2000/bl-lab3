package ru.itmo.bllab3cashbackservice.service

import org.springframework.jms.core.JmsTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import ru.itmo.bllab3cashbackservice.controller.CashbackChangeRequestPayload
import ru.itmo.bllab3cashbackservice.repo.CashbackRepository
import ru.itmo.bllab3messages.CashbackChangeStatusRequest
import ru.itmo.bllab3messages.CashbackStatus
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class CashbackService(
        private val cashbackRepository: CashbackRepository,
        private val messageService: MessageService,
        private val template: TransactionTemplate,
        private val jmsTemplate: JmsTemplate
) {
    @Scheduled(cron = "0 0 15 * * ?")
    fun processCashbacks() {
        template.execute {
            cashbackRepository.findByStartDateLessThan(LocalDateTime.now().minusDays(60)).forEach { cashback ->
                if (cashback.status != CashbackStatus.NEW && cashback.isPaid && cashback.isOrderCompleted && cashback.confirmPayment) {
                    cashback.status = CashbackStatus.APPROVED
                    messageService.sendNotificationToClient("Кэшбек подтвержден", cashback.client)
                } else {
                    cashback.status = CashbackStatus.REJECTED
                    messageService.sendNotificationToClient("Кэшбек отклонен", cashback.client)
                }

                jmsTemplate.convertAndSend("ChangeCashbackStatusQueue",
                        CashbackChangeStatusRequest(cashback.id, cashback.status, cashback.cashbackSum) )
                cashbackRepository.save(cashback)
            }
        }
    }

    fun updateCashback(payload: CashbackChangeRequestPayload) {
        template.execute {
            val cashback = cashbackRepository.findById(payload.cashbackId).orElseThrow {
                EntityNotFoundException("Кэшбек с id ${payload.cashbackId} не найден!")
            }
            payload.isPaid?.let { cashback.isPaid = it }
            payload.isOrderCompleted?.let { cashback.isOrderCompleted = it }
            payload.payment?.let {
                cashback.confirmPayment = true
                cashback.cashbackSum = it
            }
            if (cashback.status == CashbackStatus.NEW)
                cashback.status = CashbackStatus.RECEIVED_INF
            cashbackRepository.save(cashback)
        }
    }
}
