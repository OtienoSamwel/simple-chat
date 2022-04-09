package com.otienosamwel.simple_chat.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.otienosamwel.simple_chat.ui.presentation.state.AudioChatState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    val appBarScrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }

    Scaffold(
        topBar = { ChatAppBar(scrollBehavior = appBarScrollBehavior) },
        modifier = Modifier.nestedScroll(connection = appBarScrollBehavior.nestedScrollConnection),
        bottomBar = { ChatMessageInput() },
        content = { Home() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val messages = AudioChatState.messages
    val scope = rememberCoroutineScope()
    val state = rememberLazyListState().also { scope.launch { it.scrollToItem(messages.lastIndex) } }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), state = state
    ) {
        items(messages) {
            if (it.message != "") Message(message = it.message, thisUser = it.thisUser)
        }

        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

@Composable
fun ChatAppBar(scrollBehavior: TopAppBarScrollBehavior) {
    SmallTopAppBar(title = { Text(text = "Chat App") }, scrollBehavior = scrollBehavior)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatMessageInput() {
    OutlinedTextField(
        value = AudioChatState.message,
        onValueChange = { AudioChatState.message = it },
        trailingIcon = {
            if (AudioChatState.message != "" && AudioChatState.message.isNotBlank()) IconButton(onClick = {
                AudioChatState.outgoingMessageFlow.value = AudioChatState.message
                AudioChatState.message = ""
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "",
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp),
        shape = CircleShape,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Black,
            focusedBorderColor = Color.DarkGray
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Message(message: String, thisUser: Boolean) {
    val horizontalArrangement = if (thisUser) Arrangement.End else Arrangement.Start
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .clip(CircleShape)
        ) {
            Text(text = message, modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        }
    }
}