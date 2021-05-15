package ru.itmo.bllab3clientapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jms.annotation.EnableJms

@EnableJms
@SpringBootApplication
class BlLab3ClientappApplication

fun main(args: Array<String>) {
    runApplication<BlLab3ClientappApplication>(*args)
}
