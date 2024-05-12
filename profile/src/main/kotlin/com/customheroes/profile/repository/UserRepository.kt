package com.customheroes.profile.repository

import com.customheroes.profile.model.postgres.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface UserRepository: CrudRepository<User?, Int?> {
    fun findByLogin(userLogin: String): User?
    @Query("UPDATE user_table SET user_full_name=:user_name WHERE user_id=:user_id", nativeQuery = true)
    fun updateUserName(@Param("user_id") userId: Int, @Param("user_name") userName: String)

    @Query("UPDATE user_table SET user_phone_number=:user_phone WHERE user_id=:user_id", nativeQuery = true)
    fun updateUserPhone(@Param("user_id") userId: Int, @Param("user_phone") userPhone: String)
}