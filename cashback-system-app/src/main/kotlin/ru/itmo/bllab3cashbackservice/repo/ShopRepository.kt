package ru.itmo.bllab3cashbackservice.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3cashbackservice.model.Shop
import java.util.*

interface ShopRepository : CrudRepository<Shop, Long> {
        fun findShopByName(name: String): Optional<Shop>
}
