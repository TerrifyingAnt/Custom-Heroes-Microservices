package com.customheroes.catalog.model.postgres_model

import jakarta.persistence.*


@Entity
@Table(name = "figure_table")
data class Figure (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "figure_id")
    var id: Int? = null,

    @Column(name = "figure_name")
    var title: String? = null,

    @Column(name = "figure_description")
    var description: String? = null,

    @Column(name = "figure_price")
    var price: Double? = null,

    @Column(name = "figure_rating")
    var rating: Double? = null,

    @Column(name = "figure_is_movable")
    var isMovable: Boolean? = null,

    @Column(name = "figure_making_time")
    var timeOfMaking: String? = null,

    @Column(name = "figure_source_path")
    var sourcePath: String? = null
)