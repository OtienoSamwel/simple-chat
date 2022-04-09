package com.otienosamwel.simple_chat

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.otienosamwel.simple_chat.domain.SocketService
import com.otienosamwel.simple_chat.ui.theme.AudioChatTheme
import com.otienosamwel.simple_chat.ui.presentation.Content

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            AudioChatTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { Content() }
            }
        }
        viewModel.connectToServer()
    }

    override fun onStop() {
        super.onStop()
        SocketService.closeClients()
    }

    companion object {
        private const val TAG = "MAIN ACTIVITY"
    }
}
