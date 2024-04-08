package com.customheroes.catalog.utils

import com.customheroes.catalog.model.dto.FigureDto
import com.customheroes.catalog.model.dto.FigurePreviewDto
import com.customheroes.catalog.model.dto.FigureWithoutLinks
import com.customheroes.catalog.model.postgres_model.Figure
import com.customheroes.catalog.model.postgres_model.Filter
import com.customheroes.catalog.model.postgres_model.Tag

fun Figure?.toFigureDto(listTags: List<Tag?>): FigureWithoutLinks {
    val nonNullListTags = mutableListOf<Tag>()
    listTags.forEach { tag ->
        if (tag != null) {
            nonNullListTags.add(tag)
        }
    }
    return FigureWithoutLinks (
            id = this?.id ?: -1,
            title = this?.title ?: "",
            description = this?.description ?: "",
            price = (this?.price ?: 1.0).toFloat(),
            rating = (this?.rating ?: 1.0).toFloat(),
            isMovable = this?.isMovable ?: false,
            timeOfMaking = this?.timeOfMaking ?: "-1 день",
            sourcePath = this?.sourcePath ?: "xd/xd/xd",
            tags = nonNullListTags
    )
}

fun Filter?.toFigureDto(): FigureWithoutLinks {
    return FigureWithoutLinks (
            id = this?.figure?.id ?: -1,
            title = this?.figure?.title ?: "",
            description = this?.figure?.description ?: "",
            price = (this?.figure?.price ?: 1.0).toFloat(),
            rating = (this?.figure?.rating ?: 1.0).toFloat(),
            isMovable = this?.figure?.isMovable ?: false,
            timeOfMaking = this?.figure?.timeOfMaking ?: "-1 день",
            sourcePath = this?.figure?.sourcePath ?: "xd/xd/xd",
            tags = mutableListOf(this?.tag ?: Tag(-1, "xd"))
    )
}

fun FigureWithoutLinks.toFigureDto(imageLinks: List<String>, modelLink: String): FigureDto {
    return FigureDto(
            this.id,
            this.title,
            this.description,
            this.price,
            this.rating,
            this.isMovable,
            this.timeOfMaking,
            imageLinks,
            modelLink,
            this.tags
    )
}

fun FigureDto.toFigurePreviewDto(): FigurePreviewDto {
    return FigurePreviewDto(
        this.id,
        this.title,
        this.price,
        this.imageLinks,
        this.tags
    )
}