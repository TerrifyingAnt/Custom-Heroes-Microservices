package com.customheroes.order.controller

import com.customheroes.order.model.Figure
import com.customheroes.order.model.Order
import com.customheroes.order.model.OrderItem
import com.customheroes.order.model.User
import com.customheroes.order.repository.FigureRepository
import com.customheroes.order.repository.OrderItemRepository
import com.customheroes.order.repository.OrderRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@RestController
@RequestMapping("/order")
class OrderController {
    @Autowired
    var figureRepository: FigureRepository? = null

    @Autowired
    var orderItemRepository: OrderItemRepository? = null

    @Autowired
    var orderRepository: OrderRepository? = null

    @Autowired
    var userRepository: UserRepository? = null


    @GetMapping("/xd")
    fun test(): String {
        return "тест"
    }

    @PostMapping("/createOrder")
    fun createOrder(@RequestBody order: String, request1: HttpServletRequest?) {
    }

    @PostMapping("/getOrders")
    fun getOrders(request: HttpServletRequest?): List<OrderItem?> {
        return listOf()
    }
}
