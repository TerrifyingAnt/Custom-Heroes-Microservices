package com.customheroes.order.repository

import com.customheroes.order.model.OrderItem
import org.springframework.data.repository.CrudRepository


interface OrderItemRepository : CrudRepository<OrderItem?, Int?> {
    fun findAllByOrderId(id: Int?): List<OrderItem?>?
}
