package com.customheroes.profile.model.postgres

import jakarta.persistence.*

@Entity
@Table(name = "user_table")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Int? = null,

    @Column(name = "user_login")
    var login: String? = null,

    @Column(name = "user_password")
    var password: String? = null,

    @Column(name = "user_type")
    var type: String? = null,

    @Column(name = "user_phone_number")
    var phoneNumber: String? = null,

    @Column(name = "user_full_name")
    var userFullName: String? = null,

    @Column(name = "user_avatar_source_path")
    var avatarPath: String? = null

)