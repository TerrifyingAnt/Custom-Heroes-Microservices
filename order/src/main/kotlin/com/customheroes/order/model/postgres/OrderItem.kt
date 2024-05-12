package com.customheroes.order.model.postgres

import jakarta.persistence.*


@Entity
@Table(name = "order_item_table")
data class OrderItem (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    var id: Int? = null,

    @Column(name = "type")
    var type: Int? = null,

    @Column(name = "order_item_count")
    var count: Int? = null,

    @Column(name = "order_item_price")
    var price: Float? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id")
    var order: Order? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "figure_id")
    var figure: Figure? = null,

    @Column(name = "order_item_description")
    var orderItemDescription: String? = null,

    @Column(name = "order_item_reference")
    var orderItemReference: String? = null,

    @Column(name = "order_item_movable")
    var orderItemMovable: Boolean? = null,

    @Column(name = "order_item_colored")
    var orderItemColored: Boolean? = null,

    @Column(name = "order_item_hair_link")
    var orderItemHairLink: String? = null,

    @Column(name = "order_item_eye_link")
    var orderItemEyeLink: String? = null,

    @Column(name = "order_item_body_link")
    var orderItemBodyLink: String? = null,

    @Column(name = "order_item_title")
    var orderItemTitle: String? = null,
)