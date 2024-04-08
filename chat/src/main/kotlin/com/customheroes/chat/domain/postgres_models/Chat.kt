package com.customheroes.chat.domain.postgres_models

import jakarta.persistence.*
import org.springframework.context.annotation.Primary

@Entity
@Table(name = "chat_table")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    val chatId: Int,

    @Column(name = "chat_creation_date")
    val chatCreationDate: String,

    @Column(name = "chat_last_message_data")
    val chatLastMessageData: String,

    @Column(name = "chat_last_message_is_viewed")
    val chatLastMessageIsViewed: Boolean,

    @Column(name = "chat_last_message_who_sent")
    val chatLastMessageWhoSent: Int
)
