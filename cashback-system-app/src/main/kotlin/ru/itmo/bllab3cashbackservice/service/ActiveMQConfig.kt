package ru.itmo.bllab3cashbackservice.service

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import javax.jms.ConnectionFactory


@Configuration
class ActiveMQConfig {
    @Value("\${spring.activemq.broker-url}")
    private val brokerUrl: String? = null

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()
        activeMQConnectionFactory.brokerURL = brokerUrl
        return activeMQConnectionFactory
    }

    @Bean
    fun jmsTemplate(): JmsTemplate? {
        val jmsTemplate = JmsTemplate()
        jmsTemplate.connectionFactory = connectionFactory()
//        jmsTemplate.isPubSubDomain = true // enable for Pub Sub to topic. Not Required for Queue.
        return jmsTemplate
    }

    @Bean
    fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory? {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory())
        factory.setPubSubDomain(true)
        return factory
    }
}
