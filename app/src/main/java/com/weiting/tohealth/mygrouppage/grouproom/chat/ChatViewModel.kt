package com.weiting.tohealth.mygrouppage.grouproom.chat

import androidx.lifecycle.*
import com.weiting.tohealth.data.*
import kotlinx.coroutines.launch

class ChatViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    group: Group
) : ViewModel() {

    private val memberList = group.member
    private val newList = mutableListOf<WhoseMessage>()

    private val chatList =
        firebaseDataRepository.getLiveChatMessages(UserManager.UserInfo.id ?: "", group.id ?: "")

    val chatMediatorLiveData = MediatorLiveData<MutableList<WhoseMessage>>().apply {
        addSource(chatList) {
            newList.clear()
            it.forEach { chat ->
                viewModelScope.launch {
                    when (chat.creator == UserManager.UserInfo.id) {
                        true -> {
                            newList.add(createSelfMessages(chat))
                        }

                        false -> {
                            newList.add(createOthersMessages(chat))
                        }
                    }
                    value = newList
                }
            }
        }
    }

    private fun createOthersMessages(chat: Chat): WhoseMessage {
        var data = Member()

        memberList.forEach {
            if (chat.creator == it.userId) {
                data = it
            }
        }
        return WhoseMessage.OthersMessage(chat, data)
    }

    private fun createSelfMessages(chat: Chat): WhoseMessage {
        var data = Member()

        memberList.forEach {
            if (chat.creator == it.userId) {
                data = it
            }
        }
        return WhoseMessage.SelfMessage(chat, data)
    }

    fun postMessage(chat: Chat) {
        firebaseDataRepository.postChatMessage(chat)
    }
}

sealed class WhoseMessage {

    data class SelfMessage(val chat: Chat, val member: Member) : WhoseMessage()

    data class OthersMessage(val chat: Chat, val member: Member) : WhoseMessage()
}
