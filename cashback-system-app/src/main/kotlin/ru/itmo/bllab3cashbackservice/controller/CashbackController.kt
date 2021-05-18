package ru.itmo.bllab3cashbackservice.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.bllab3cashbackservice.model.*
import ru.itmo.bllab3cashbackservice.repo.CashbackRepository
import ru.itmo.bllab3cashbackservice.repo.ClientRepository
import ru.itmo.bllab3cashbackservice.repo.ShopRepository
import ru.itmo.bllab3cashbackservice.service.UserService
import ru.itmo.bllab3messages.CashbackStatus
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/cashback/")
@RestController
class CashbackController(
        private val cashbackRepository: CashbackRepository,
        private val clientRepository: ClientRepository,
        private val shopRepository: ShopRepository,
        private val userService: UserService
) {

    companion object {
        fun mapCashbackDataForShop (cashback: Cashback): CashbackDataForShop =
                CashbackDataForShop(cashback.id, cashback.startDate, cashback.client.firstName, cashback.client.lastName,
                cashback.isPaid, cashback.isOrderCompleted, cashback.confirmPayment, cashback.status, cashback.cashbackSum)
        fun mapCashbackData(cashback: Cashback):  CashbackData =
            CashbackData(cashback.id, cashback.startDate, cashback.client.firstName, cashback.client.lastName, cashback.shop.name,
                    cashback.isPaid, cashback.isOrderCompleted, cashback.confirmPayment, cashback.status, cashback.cashbackSum)
    }

    @GetMapping("status/{cashbackId}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP')")
    fun getCashbackStatus(@PathVariable cashbackId: Long): CashbackStatus {
        userService.checkShopAuthority(cashbackId)
        return cashbackRepository.findById(cashbackId).orElseThrow {
            EntityNotFoundException("Кэшбек с id $cashbackId не найден!")
        }.status
    }

    @GetMapping("{cashbackId}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP')")
    fun getCashback(@PathVariable cashbackId: Long): CashbackData {
        userService.checkShopAuthority(cashbackId)
        val cashback = cashbackRepository.findById(cashbackId).orElseThrow {
            EntityNotFoundException("Кэшбек с id $cashbackId не найден!")
        }
        return mapCashbackData(cashback)
    }

    @GetMapping("findByClient/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getClientCashbacks(@PathVariable clientId: Long): Iterable<CashbackData> {
        val client = clientRepository.findById(clientId).orElseThrow {
            EntityNotFoundException("Клиент с id $clientId не найден!")
        }
        return cashbackRepository.findCashbackByClient(client)
                .map { cashback: Cashback -> mapCashbackData(cashback) }
    }

    @GetMapping("findByShop/{shopId}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP')")
    fun getShopCashbacks(@PathVariable shopId: Long): Iterable<CashbackDataForShop> {
        userService.checkShopAuthority(shopId)
        val shop = shopRepository.findById(shopId).orElseThrow {
            EntityNotFoundException("Магазин с id $shopId не найден!")
        }
        return cashbackRepository.findCashbackByShop(shop)
                .map { cashback: Cashback -> mapCashbackDataForShop(cashback) }
    }

}
