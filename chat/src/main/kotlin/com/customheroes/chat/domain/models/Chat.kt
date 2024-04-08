package com.customheroes.chat.domain.models

data class Chat(
    val chatId: Int,
    val chatLastMessage: String,
    val chatLastMessageIsViewed: Boolean,
    val chatLastMessageWhoSent: Int
)