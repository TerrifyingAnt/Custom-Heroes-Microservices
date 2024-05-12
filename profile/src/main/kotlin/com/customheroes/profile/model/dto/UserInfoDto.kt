package com.customheroes.profile.model.dto

data class UserInfoDto(
    val id: Int,
    val userFullName: String,
    val userPhone: String,
    val userLogin: String,
    val userAvatarLink: String
)