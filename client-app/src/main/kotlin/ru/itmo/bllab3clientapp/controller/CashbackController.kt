package ru.itmo.bllab3clientapp.controller

import org.springframework.jms.core.JmsTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.bllab3clientapp.model.*
import ru.itmo.bllab3clientapp.repo.CashbackRepository
import ru.itmo.bllab3clientapp.repo.ClientRepository
import ru.itmo.bllab3clientapp.repo.ShopRepository
import ru.itmo.bllab3clientapp.service.UserService
import ru.itmo.bllab3messages.CashbackDataForService
import ru.itmo.bllab3messages.CashbackStatus
import javax.persistence.EntityNotFoundException

data class CashbackRequestPayload(
        val clientId: Long,
        val shopName: String,
)

data class CashbackResponse(
        val message: String,
        val id: Long? = null,
)

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/cashback/")
@RestController
class CashbackController(
        private val cashbackRepository: CashbackRepository,
        private val clientRepository: ClientRepository,
        private val userService: UserService,
        private val shopRepository: ShopRepository,
        private val jmsTemplate: JmsTemplate
) {

    companion object {
        fun mapCashbackDataForClient (cashback: Cashback): CashbackDataForClient =
                CashbackDataForClient(cashback.id, cashback.startDate, cashback.shop.name,
                        cashback.status, cashback.cashbackSum)
        fun mapCashbackData(cashback: Cashback):  CashbackData =
            CashbackData(cashback.id, cashback.startDate, cashback.client.firstName, cashback.client.lastName,
                    cashback.shop.name, cashback.status, cashback.cashbackSum)
    }

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('CLIENT')")
    fun createCashback(@RequestBody payload: CashbackRequestPayload): CashbackResponse {
        val client = clientRepository.findById(payload.clientId).orElseThrow {
            EntityNotFoundException("Клиент с id ${payload.clientId} не найден!")
        }
        val shop = shopRepository.findShopByName(payload.shopName).orElseThrow {
            EntityNotFoundException("Магазин с названием ${payload.shopName} не найден!")
        }

        val cashback = Cashback(0, client = client, shop = shop)
        cashbackRepository.save(cashback)
        jmsTemplate.convertAndSend("CreateCashbackQueue", CashbackDataForService(cashback.id, cashback.startDate, cashback.client.id, cashback.shop.id))
        return CashbackResponse("Заявка на получение кэшбека для клиента " +
                "${client.firstName} ${client.lastName} от магазина ${shop.name} была создана.", cashback.id)
    }

    @GetMapping("status/{cashbackId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    fun getCashbackStatus(@PathVariable cashbackId: Long): CashbackStatus {
        userService.checkClientAuthority(cashbackId)
        return cashbackRepository.findById(cashbackId).orElseThrow {
            EntityNotFoundException("Кэшбек с id $cashbackId не найден!")
        }.status
    }

    @GetMapping("{cashbackId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    fun getCashback(@PathVariable cashbackId: Long): CashbackData {
        userService.checkClientAuthority(cashbackId)
        val cashback = cashbackRepository.findById(cashbackId).orElseThrow {
            EntityNotFoundException("Кэшбек с id $cashbackId не найден!")
        }
        return mapCashbackData(cashback)
    }

    @GetMapping("findByClient/{clientId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    fun getClientCashbacks(@PathVariable clientId: Long): Iterable<CashbackDataForClient> {
        userService.checkClientAuthority(clientId)
        val client = clientRepository.findById(clientId).orElseThrow {
            EntityNotFoundException("Клиент с id $clientId не найден!")
        }
        return cashbackRepository.findCashbackByClient(client)
                .map { cashback: Cashback -> mapCashbackDataForClient(cashback) }
    }

}
