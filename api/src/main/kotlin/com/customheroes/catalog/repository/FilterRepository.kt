package com.customheroes.catalog.repository

import com.customheroes.catalog.model.postgres_model.Figure
import com.customheroes.catalog.model.postgres_model.Filter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface FilterRepository : CrudRepository<Filter?, Int?> {
    fun findByFigure(figure: Figure?): List<Filter?>?

    @Query(value = "SELECT DISTINCT filter_id, filter_table.figure_id, filter_table.tag_id FROM filter_table JOIN figure_table ON filter_table.figure_id = figure_table.figure_id WHERE tag_id=:tag_id AND Function…figure_table.figure_name) LIKE CONCAT('%', LOWER(:figure_title),'%')", nativeQuery = true)
    fun findAllByTagAndFigureTitle(pageable: Pageable, @Param("tag_id") tagId: Int, @Param("figure_title") figureTitle: String): List<Filter?>?

    @Query(value = "SELECT DISTINCT filter_id, filter_table.figure_id, filter_table.tag_id FROM filter_table JOIN figure_table ON filter_table.figure_id = figure_table.figure_id WHERE LOWER(figure_table.figure_name) LIKE CONCAT('%', LOWER(:figure_title),'%')", nativeQuery = true)
    fun findAllByFigureTitle(pageable: Pageable, @Param("figure_title") figureTitle: String): Page<Filter?>?
    @Query(value = "SELECT * FROM filter_table WHERE figure_id=?1", nativeQuery = true)
    fun findByFigureId(figureId: Int): List<Filter?>?

}