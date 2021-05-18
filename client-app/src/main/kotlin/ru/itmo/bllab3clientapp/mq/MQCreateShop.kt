package ru.itmo.bllab3clientapp.mq

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import ru.itmo.bllab3clientapp.model.Shop
import ru.itmo.bllab3clientapp.repo.ShopRepository
import ru.itmo.bllab3messages.ShopDataForClient
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.ObjectMessage

@Service
class MQCreateShop (
        private val shopRepository: ShopRepository,
): MessageListener {

    @JmsListener(destination = "CreateShopQueue")
    override fun onMessage(message: Message?) {
        try {
            val objectMessage: ObjectMessage = message as ObjectMessage
            val shop: ShopDataForClient = objectMessage.getObject() as ShopDataForClient
            shopRepository.save(Shop(shop.id, shop.name))
        } catch (e: Exception) {
            System.out.println(e)
        }
    }
}
