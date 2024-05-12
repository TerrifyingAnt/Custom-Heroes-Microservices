package com.customheroes.order.util

import com.customheroes.order.model.OrderStatus
import com.customheroes.order.model.dto.OrderInfoDto
import com.customheroes.order.model.dto.OrderItemDto
import com.customheroes.order.model.dto.OrderItemEntityDto
import com.customheroes.order.model.postgres.Figure
import com.customheroes.order.model.postgres.Order
import com.customheroes.order.model.postgres.OrderItem
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

fun OrderItemEntityDto.toOrderItem(order: Order, figure: Figure): OrderItem {
    return OrderItem(
        id = 0,
        type = this.type,
        count = this.count,
        price = this.price,
        order = order,
        figure = figure,
        orderItemDescription = this.description,
        orderItemReference = this.references,
        orderItemMovable = this.movable,
        orderItemColored = this.colored,
        orderItemHairLink = this.hairLink,
        orderItemEyeLink = this.eyeLink,
        orderItemBodyLink = this.bodyLink,
        orderItemTitle = this.title,
    )
}

fun Order.toOrderInfoDto(): OrderInfoDto {
    return OrderInfoDto(
        orderId = this.id ?: 1,
        state = this.state ?: OrderStatus.ACCEPT.name,
        date = this.date ?: ""
    )
}

fun OrderItem.toOrderItemDto(): OrderItemDto {
    return OrderItemDto(
        type = this.type ?: 2,
        count = this.count ?: 1,
        price = this.price ?: 5000f,
        title = this.orderItemTitle ?: "тестовая"
    )
}