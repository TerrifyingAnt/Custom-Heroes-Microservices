package com.customheroes.auth.model.dto

data class UserRequestDto(
    val login: String,
    val password: String,
    val fullName: String
)
