package com.customheroes.order.model.dto

import com.customheroes.order.model.postgres.Order
import com.customheroes.order.model.postgres.OrderItem

data class OrderDto (
    val orderInfo: OrderInfoDto,
    val orderDetails: List<OrderItemDto>
)