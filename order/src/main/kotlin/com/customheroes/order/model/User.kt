package com.customheroes.order.model

import jakarta.persistence.*


@Entity
@Table(name = "user_table")
class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "user_full_name")
    var name: String? = null

    @Column(name = "user_login")
    var login: String? = null

    @Column(name = "user_password")
    var password: String? = null

    @Column(name = "user_phone_number")
    var phoneNumber: String? = null

    @Column(name = "user_avatar_source_path")
    var avatarSourcePath: String? = null

    @Column(name = "user_type")
    var type: String? = null
}
