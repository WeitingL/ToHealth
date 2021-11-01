package com.weiting.tohealth.mygrouppage.grouproom.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Chat
import com.weiting.tohealth.data.FirebaseRepository
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.UserManager
import kotlin.system.measureTimeMillis

class ChatViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    private val _chatMessages = MutableLiveData<List<WhoseMessage>>()
    val chatMessages: LiveData<List<WhoseMessage>>
        get() = _chatMessages

    val chatList = firebaseDataRepository.getLiveChatMessage(UserManager.userId, group.id!!)

    fun identifyUser(list: List<Chat>) {
        val newList = mutableListOf<WhoseMessage>()

        list.forEach {
            when (it.creator == UserManager.userId) {
                true -> {
                    it.creator = idChangeToName(it.creator!!)
                    newList += WhoseMessage.SelfMessage(it)
                }
                false -> {
                    it.creator = idChangeToName(it.creator!!)
                    newList += WhoseMessage.OthersMessage(it)
                }
            }
        }

        _chatMessages.value = newList
    }

    fun postMessage(chat: Chat) {
        firebaseDataRepository.postChatMessage(chat)
    }

    private fun idChangeToName(idInChat: String):String{
        val nickname = group.member.forEach {
            if(it.userId == idInChat){
                return it.nickName?:"成員"
            }
        }
        return nickname.toString()
    }

}

sealed class WhoseMessage {

    data class SelfMessage(val chat: Chat) : WhoseMessage()

    data class OthersMessage(val chat: Chat) : WhoseMessage()

}