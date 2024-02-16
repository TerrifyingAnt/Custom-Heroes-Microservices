package com.customheroes.catalog.repository

import com.customheroes.catalog.model.postgres_model.Figure
import org.springframework.data.repository.CrudRepository


interface FigureRepository : CrudRepository<Figure?, Int?>