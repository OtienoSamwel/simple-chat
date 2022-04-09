package com.otienosamwel.simple_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otienosamwel.simple_chat.domain.SocketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    fun connectToServer() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { SocketService.connectToChatSocket() }
        }
    }
}