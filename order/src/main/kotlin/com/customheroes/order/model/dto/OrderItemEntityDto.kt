package com.customheroes.order.model.dto

data class OrderItemEntityDto (
    val type: Int = 0, // тип фигуры: с каталога, кастомная по фото, кастомная по описанию
    val figureId: Int = 0, // id фигуры с каталога
    val description: String = "", // описание кастомной по тексту
    val references: String = "", // референсные ссылки
    val movable: Boolean = false, // двигается
    val colored: Boolean = false, // раскрасить
    val hairLink: String = "", // ссылка на модельку волос
    val eyeLink: String = "", // ссылка на модельку глаз
    val bodyLink: String = "", // ссылка на тело
    var count: Int = 0, // количество фигурки
    val price: Float = 0f, // цена фигурки
    val title: String = "" // название фигурки
)


