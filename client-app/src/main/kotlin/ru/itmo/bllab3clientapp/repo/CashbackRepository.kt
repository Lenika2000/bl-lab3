package ru.itmo.bllab3clientapp.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3clientapp.model.Cashback
import ru.itmo.bllab3clientapp.model.Client
import ru.itmo.bllab3clientapp.model.Shop
import java.time.LocalDateTime

interface CashbackRepository : CrudRepository<Cashback, Long> {
    fun findCashbackByClient(client: Client): List<Cashback>
}
