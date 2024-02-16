package com.customheroes.catalog.model.dto

import com.customheroes.catalog.model.postgres_model.Tag

data class FigureWithoutLinks (
        val id: Int,
        val title: String,
        val description: String,
        val price: Float,
        val rating: Float,
        val isMovable: Boolean,
        val timeOfMaking: String,
        val sourcePath: String,
        val tags: MutableList<Tag>
)