package com.customheroes.order.model

import jakarta.persistence.*


@Entity
@Table(name = "order_item_table")
class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    var id: Int? = null

    @Column(name = "order_item_count")
    var count: Int? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id")
    var order: Order? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "figure_id")
    var figure: Figure? = null
}