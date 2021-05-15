package ru.itmo.bllab3clientapp.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3clientapp.model.Client

interface ClientRepository : CrudRepository<Client, Long>
