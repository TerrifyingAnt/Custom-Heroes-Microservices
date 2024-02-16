package com.customheroes.catalog.repository

import com.customheroes.catalog.model.postgres_model.Tag
import org.springframework.data.repository.CrudRepository


interface TagRepository : CrudRepository<Tag?, Int?> {
    fun findByTitle(title: String): Tag?
}
