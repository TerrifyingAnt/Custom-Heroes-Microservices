package com.customheroes.order.model.postgres

import jakarta.persistence.*
import jakarta.persistence.CascadeType;
import java.util.*


@Entity
@Table(name = "order_table")
data class Order (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    var id: Int? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Column(name = "order_status")
    var state: String? = null,

    @Column(name = "order_date")
    var date: String? = null
)

