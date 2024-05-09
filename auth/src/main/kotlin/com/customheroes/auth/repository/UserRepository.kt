package com.customheroes.auth.repository

import com.customheroes.auth.model.postgres.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User?, Int?> {
    fun findByLogin(userLogin: String): User?
}