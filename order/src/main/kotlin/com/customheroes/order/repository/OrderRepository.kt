package com.customheroes.order.repository

import com.customheroes.order.model.Order
import org.springframework.data.repository.CrudRepository


interface OrderRepository : CrudRepository<Order?, Int?> {
    fun findByUserId(id: Int?): List<Order?>?
}
