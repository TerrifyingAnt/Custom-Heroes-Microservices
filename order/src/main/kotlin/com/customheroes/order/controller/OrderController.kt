package com.customheroes.order.controller

import com.customheroes.order.model.OrderStatus
import com.customheroes.order.model.dto.OrderDto
import com.customheroes.order.model.dto.OrderItemEntityDto
import com.customheroes.order.model.postgres.Figure
import com.customheroes.order.model.postgres.Order
import com.customheroes.order.repository.FigureRepository
import com.customheroes.order.repository.OrderItemRepository
import com.customheroes.order.repository.OrderRepository
import com.customheroes.order.repository.UserRepository
import com.customheroes.order.util.toOrderInfoDto
import com.customheroes.order.util.toOrderItem
import com.customheroes.order.util.toOrderItemDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*


@RestController
@RequestMapping("/orders")
class OrderController {

    @Autowired
    var orderItemRepository: OrderItemRepository? = null

    @Autowired
    var orderRepository: OrderRepository? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var figureRepository: FigureRepository? = null

    @PostMapping("/create_order")
    fun createOrder(@RequestHeader("x-user") userId: String, @RequestBody order: List<OrderItemEntityDto>) {
        val nonNullOrderRepository = orderRepository ?: return
        val nonNullUserRepository = userRepository ?: return
        val nonNullOrderItemRepository = orderItemRepository ?: return
        val nonNullFigureRepository = figureRepository ?: return
        val userPostgresId = userId.split(":").last().toInt()
        val user = nonNullUserRepository.findById(userPostgresId).get()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        nonNullOrderRepository.save(Order(id = 0, user = user, OrderStatus.ACCEPT.name, currentDate))
        val orderId = nonNullOrderRepository.getLatestOrder() ?: return
        order.forEach { item ->
            val figureId = if (item.figureId == 0) 1 else item.figureId
            val figure = nonNullFigureRepository.findById(figureId)
            if (!figure.isEmpty) {
                nonNullOrderItemRepository.save(item.toOrderItem(orderId, figure.get()))
            }
        }

    }

    @GetMapping("/get_orders")
    fun getOrders(@RequestHeader("x-user") userId: String): List<OrderDto>? {
        val nonNullOrderRepository = orderRepository ?: return null
        val nonNullOrderItemRepository = orderItemRepository ?: return null
        val postgresUserId = userId.split(":").last().toInt()
        val orderList = nonNullOrderRepository.getAllUserOrders(postgresUserId) ?: return null
        val responseList = mutableListOf<OrderDto>()
        orderList.forEach { order ->
            val orderDetails = nonNullOrderItemRepository.findAllByOrderId(order.id) ?: listOf()
            responseList.add(OrderDto(order.toOrderInfoDto(), orderDetails.filterNotNull().map{ it.toOrderItemDto()}))
        }
        return responseList
    }
}
