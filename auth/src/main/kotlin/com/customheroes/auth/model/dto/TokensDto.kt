package com.customheroes.auth.model.dto

data class TokensDto (
    val accessToken: String,
    val refreshToken: String
)