package ru.itmo.bllab3cashbackservice.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.bllab3cashbackservice.repo.*
import ru.itmo.bllab3cashbackservice.service.CashbackService
import ru.itmo.bllab3cashbackservice.service.UserService

data class CashbackChangeRequestPayload(
        val cashbackId: Long,
        val isPaid: Boolean?,
        val isOrderCompleted: Boolean?,
        val payment: Double?,
)

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/shop/cashback/")
@RestController
class CashbackShopController(
        private val cashbackService: CashbackService,
        private val userService: UserService
) {

    @PostMapping("update")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP')")
    fun updateCashback(@RequestBody payload: CashbackChangeRequestPayload): CashbackResponse {
        userService.checkShopAuthority(payload.cashbackId)
        cashbackService.updateCashback(payload)
        return CashbackResponse("Изменение информации о кэшбэке принято к обработке")
    }

}
