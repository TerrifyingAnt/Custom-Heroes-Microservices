package com.customheroes.profile.util

import com.customheroes.profile.model.dto.UserInfoDto
import com.customheroes.profile.model.postgres.User

fun User.toUserInfoDto(avatarPath: String): UserInfoDto {
    return UserInfoDto(
        this.id ?: 0,
        this.userFullName ?: "test_user",
        this.phoneNumber ?: "88005553535",
        this.login ?: "test_login",
        avatarPath
    )
}