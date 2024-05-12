package com.customheroes.order.model.dto

import jakarta.persistence.Column

data class OrderInfoDto(
    val orderId: Int,
    var state: String,
    var date: String
)
