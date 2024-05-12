package com.customheroes.order.repository

import com.customheroes.order.model.postgres.Order
import com.customheroes.order.model.postgres.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User?, Int?> {
}