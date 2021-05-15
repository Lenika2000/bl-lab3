package ru.itmo.bllab3cashbackservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jms.annotation.EnableJms

@EnableJms
@SpringBootApplication
class BlLab3CashbackServiceApplication

fun main(args: Array<String>) {
    runApplication<BlLab3CashbackServiceApplication>(*args)
}
