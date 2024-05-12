package com.customheroes.order.repository

import com.customheroes.order.model.dto.OrderItemEntityDto
import com.customheroes.order.model.postgres.OrderItem
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface OrderItemRepository : CrudRepository<OrderItem?, Int?> {
    fun findAllByOrderId(id: Int?): List<OrderItem?>?
}
