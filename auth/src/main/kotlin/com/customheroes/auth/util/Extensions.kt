package com.customheroes.auth.util

import com.customheroes.auth.model.dto.UserInfoDto
import com.customheroes.auth.model.postgres.User

fun User.toUserInfoDto(): UserInfoDto {
    return UserInfoDto(
        this.id ?: 0,
        this.userFullName ?: "test_user",
        this.phoneNumber ?: "88005553535",
        this.login ?: "test_login",
        this.avatarPath ?: ""
    )
}