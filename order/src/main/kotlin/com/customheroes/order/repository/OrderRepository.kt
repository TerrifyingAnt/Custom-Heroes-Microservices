package com.customheroes.order.repository

import com.customheroes.order.model.postgres.Order
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface OrderRepository : CrudRepository<Order?, Int?> {
    fun findByUserId(id: Int?): List<Order?>?

    @Query("SELECT * FROM order_table ORDER BY order_date DESC LIMIT 1", nativeQuery = true)
    fun getLatestOrder(): Order?

    @Query("SELECT * FROM order_table WHERE user_id=:user_id", nativeQuery = true)
    fun getAllUserOrders(@Param("user_id") userId: Int): List<Order>?
}
