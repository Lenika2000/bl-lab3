package ru.itmo.bllab3cashbackservice.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.bllab3cashbackservice.service.CashbackService

data class CashbackResponse(
        val message: String,
        val id: Long? = null,
)

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/admin/cashback/")
@RestController
class CashbackAdminController(
        private val cashbackService: CashbackService,
) {

    @PostMapping("process")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun processCashback(): CashbackResponse {
        cashbackService.processCashbacks();
        return CashbackResponse("Завершена обработка выплат кэшбека для покупок, совершенных более 60 дней назад")
    }
}
