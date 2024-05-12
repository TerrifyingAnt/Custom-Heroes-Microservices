package com.customheroes.order.repository

import com.customheroes.order.model.postgres.Figure
import org.springframework.data.repository.CrudRepository

interface FigureRepository : CrudRepository<Figure?, Int?>