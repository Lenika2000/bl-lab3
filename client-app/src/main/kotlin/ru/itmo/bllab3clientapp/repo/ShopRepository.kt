package ru.itmo.bllab3clientapp.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3clientapp.model.Shop
import java.util.*

interface ShopRepository : CrudRepository<Shop, Long> {
        fun findShopByName(name: String): Optional<Shop>
}
