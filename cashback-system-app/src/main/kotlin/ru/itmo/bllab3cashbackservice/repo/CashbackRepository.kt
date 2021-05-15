package ru.itmo.bllab3cashbackservice.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3cashbackservice.model.Cashback
import ru.itmo.bllab3cashbackservice.model.Client
import ru.itmo.bllab3cashbackservice.model.Shop
import java.time.LocalDateTime

interface CashbackRepository : CrudRepository<Cashback, Long> {
    fun findCashbackByClient(client: Client): List<Cashback>
    fun findCashbackByShop(shop: Shop): List<Cashback>
    fun findByStartDateLessThan(date: LocalDateTime): List<Cashback>
}
