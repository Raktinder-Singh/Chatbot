package com.rakt.heybot

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "Here you write the model name",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question: String){
        viewModelScope.launch {

            val chat = generativeModel.startChat(
                history = messageList.map {
                    content(it.role){text(it.message)}
                }.toList()
            )

            messageList.add(MessageModel(question, "user"))

            val response = chat.sendMessage(question)
//            messageList.removeLast()
            messageList.add(MessageModel(response.text.toString(), "model"))

        }
    }
}