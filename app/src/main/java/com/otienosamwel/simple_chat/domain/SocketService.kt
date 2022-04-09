package com.otienosamwel.simple_chat.domain

import android.util.Log
import com.otienosamwel.simple_chat.ui.presentation.state.AudioChatState
import com.otienosamwel.simple_chat.ui.presentation.state.ChatMessage
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.util.*

object SocketService {

    private const val TAG = "SOCKET SERVICE"
    private const val HOST = "0fb6-102-140-222-234.ngrok.io"
    private val user = UUID.randomUUID().toString()

    private val chatClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(WebSockets)
    }

    fun closeClients() = chatClient.close()

    suspend fun connectToChatSocket() {
        chatClient.use {
            try {
                it.webSocket(method = HttpMethod.Get, host = HOST, path = "/groupChat/$user") {
                    val inp = launch { inputMessages() }
                    val output = launch { outputMessages() }
                    inp.join()
                    output.cancelAndJoin()
                }
            } catch (e: Exception) {
                connectToChatSocket()
                Log.e(TAG, "connectToWebSocket: failed", e)
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            for (message in incoming) {
                if (message is Frame.Text) {
                    AudioChatState.messages.add(ChatMessage(message.readText(), false))
                    Log.i(TAG, "outputMessages: ${message.readText()} saved = ${AudioChatState.messages}")
                }
            }
        } catch (e: Exception) {
            connectToChatSocket()
            Log.e(TAG, "outputMessages: failed", e)
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        AudioChatState.outgoingMessageFlow.collect {
            try {
                send(it)
                AudioChatState.messages.add(ChatMessage(it, true))
                Log.i(TAG, "inputMessages: sent $it")
            } catch (e: Exception) {
                closeClients()
                Log.e(TAG, "inputMessages: not sent", e)
            }
        }
    }
}