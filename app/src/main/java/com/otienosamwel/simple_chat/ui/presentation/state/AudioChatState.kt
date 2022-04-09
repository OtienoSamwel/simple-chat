package com.otienosamwel.simple_chat.ui.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow

object AudioChatState {
    var message by mutableStateOf("")
    val outgoingMessageFlow: MutableStateFlow<String> = MutableStateFlow("")
    val messages: MutableList<ChatMessage> = mutableStateListOf(ChatMessage("", false))
}

data class ChatMessage(val message: String, val thisUser: Boolean)