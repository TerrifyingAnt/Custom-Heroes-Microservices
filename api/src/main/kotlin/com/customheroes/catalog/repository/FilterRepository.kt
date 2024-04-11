package com.customheroes.catalog.repository

import com.customheroes.catalog.model.postgres_model.Figure
import com.customheroes.catalog.model.postgres_model.Filter
import com.customheroes.catalog.model.postgres_model.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository


interface FilterRepository : CrudRepository<Filter?, Int?> {
    fun findByFigure(figure: Figure?): List<Filter?>?
    fun findAllByTag(tag: Tag): List<Filter?>?

    fun findAll(pageable: Pageable): Page<Filter?>?
    @Query(value = "SELECT * FROM filter_table WHERE figure_id=?1", nativeQuery = true)
    fun findByFigureId(figureId: Int): List<Filter?>?

}