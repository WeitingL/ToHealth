package com.weiting.tohealth.mygrouppage.grouproom.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Chat
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.UserManager

class ChatViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    private val _chatMessages = MutableLiveData<List<WhoseMessage>>()
    val chatMessages: LiveData<List<WhoseMessage>>
        get() = _chatMessages

    init {
        _chatMessages.value = listOf()
    }

    val chatList = firebaseDataRepository.getLiveChatMessage(UserManager.userId, group.id!!)

    fun identifyUser(list: List<Chat>) {
        list.forEach {
            when (it.creator == UserManager.userId) {
                true -> {
                    _chatMessages.value = _chatMessages.value?.plus(WhoseMessage.SelfMessage(it))
                }
                false -> {
                    _chatMessages.value = _chatMessages.value?.plus(WhoseMessage.OthersMessage(it))
                }
            }
        }
    }

    fun postMessage(chat: Chat) {
        firebaseDataRepository.postChatMessage(chat)
    }

}

sealed class WhoseMessage {

    data class SelfMessage(val chat: Chat) : WhoseMessage()

    data class OthersMessage(val chat: Chat) : WhoseMessage()

}