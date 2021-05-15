package ru.itmo.bllab3cashbackservice.repo

import org.springframework.data.repository.CrudRepository
import ru.itmo.bllab3cashbackservice.model.Admin

interface AdminRepository : CrudRepository<Admin, Long>
