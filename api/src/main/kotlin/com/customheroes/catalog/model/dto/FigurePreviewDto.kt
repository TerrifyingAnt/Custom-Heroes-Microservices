package com.customheroes.catalog.model.dto

import com.customheroes.catalog.model.postgres_model.Tag
import jakarta.persistence.Column

data class FigurePreviewDto(
    val id: Int,
    val title: String,
    val price: Float,
    val imageLinks: List<String>,
    val tags: MutableList<Tag>
)
