package com.customheroes.order

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderApplicationTests {

	@Mock
    private lateinit var orderItemRepository: OrderItemRepository

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var figureRepository: FigureRepository

    private lateinit var orderController: OrderController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        orderController = OrderController()
        orderController.orderItemRepository = orderItemRepository
        orderController.orderRepository = orderRepository
        orderController.userRepository = userRepository
        orderController.figureRepository = figureRepository
    }

	@Test
    fun `test createOrder`() {
        val userId = "1"
        val orderItemDto1 = OrderItemEntityDto(1, 1, 2)
        val orderItemDto2 = OrderItemEntityDto(2, 0, 3)
        val orderItemList = listOf(orderItemDto1, orderItemDto2)

        val user = User(1, "test user", "user@test.com")
        val figure1 = Figure(1, "Figure 1", 10.0)
        val figure2 = Figure(2, "Figure 2", 15.0)

        `when`(userRepository.findById(1)).thenReturn(Optional.of(user))
        `when`(figureRepository.findById(1)).thenReturn(Optional.of(figure1))
        `when`(figureRepository.findById(0)).thenReturn(Optional.of(figure2))


        orderController.createOrder(userId, orderItemList)
    }

    @Test
    fun `test getOrders`() {
        val postgresUserId = 1

        val order1 = Order(1, User(1, "test user", "test@user.com"), "ACCEPT", SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
        val order2 = Order(2, User(1, "test user", "test@user.com"), "ACCEPT", SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
        val orderList = listOf(order1, order2)

        val orderItem1 = OrderItem(1, 1, 1, 2, 10.0)
        val orderItem2 = OrderItem(2, 2, 2, 3, 15.0)
        val orderItemList = listOf(orderItem1, orderItem2)

        `when`(orderRepository.getAllUserOrders(postgresUserId)).thenReturn(orderList)
        `when`(orderItemRepository.findAllByOrderId(1)).thenReturn(listOf(orderItem1))
        `when`(orderItemRepository.findAllByOrderId(2)).thenReturn(listOf(orderItem2))

        val result = orderController.getOrders(userId)

        assertEquals(2, result?.size)
    }

	@Test
	fun contextLoads() {
	}

}
