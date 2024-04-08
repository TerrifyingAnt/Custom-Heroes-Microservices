package com.customheroes.catalog.model.dto

import com.customheroes.catalog.model.postgres_model.Tag
import jakarta.persistence.Column
data class FigurePreviewWithoutLinksDto (
    @Column(name = "id")
    val id: Int,
    @Column(name = "title")
    val title: String,
    @Column(name = "price")
    val price: Float,
    @Column(name = "sourcePath")
    val sourcePath: String,
    @Column(name = "tags")
    val tags: MutableList<Tag>
)