package com.customheroes.catalog.repository

import com.customheroes.catalog.model.dto.FigurePreviewDto
import com.customheroes.catalog.model.dto.FigurePreviewWithoutLinksDto
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

    @Query(value = "SELECT figure_id as id, figure_title as title, figure_price as price, figure_source_path as sourcePath, map (tag_id, tag_title) as tags " +
            "FROM filter_table JOIN figure_table ON filter_table.figure_id = figure_table.figure_id " +
            "JOIN tag_table on filter_table.tag_id = tag_table.tag_id " +
            "WHERE filter_table.tag_id=?1", nativeQuery = true
    )
    fun findAllFiguresByTagId(tagId: Int): List<FigurePreviewWithoutLinksDto?>?
}