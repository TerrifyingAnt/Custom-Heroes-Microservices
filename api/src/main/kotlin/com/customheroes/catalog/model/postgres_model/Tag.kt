package com.customheroes.catalog.model.postgres_model

import jakarta.persistence.*


@Entity
@Table(name = "tag_table")
data class Tag (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    var id: Int? = null,

    @Column(name = "tag_title")
    var title: String? = null
)
