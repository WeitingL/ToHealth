package com.weiting.tohealth.mygrouppage.grouproom.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.*
import kotlin.system.measureTimeMillis

class ChatViewModel(
    private val firebaseDataRepository: FirebaseRepository,
    private val group: Group
) : ViewModel() {

    private val memberList = MutableLiveData<MutableList<Member>>()
    private val newList = mutableListOf<WhoseMessage>()

    private val liveMember = firebaseDataRepository.getLiveMember(group.id!!)
    private val chatList = firebaseDataRepository.getLiveChatMessage(UserManager.UserInformation.id!!, group.id!!)


    val chatMediatorLiveData = MediatorLiveData<MutableList<WhoseMessage>>().apply {
        addSource(chatList) {
            it.forEach { chat ->
                when (chat.creator == UserManager.UserInformation.id) {
                    true -> {
                        chat.creator = changeCreatorIdToNickName(chat)
                        newList += WhoseMessage.SelfMessage(chat)
                    }

                    false -> {
                        chat.creator = changeCreatorIdToNickName(chat)
                        newList += WhoseMessage.OthersMessage(chat)
                    }
                }

                value = newList
            }
        }
        addSource(liveMember) {
//            Log.i("data!", it.toString())
            memberList.value = it.toMutableList()
        }
    }

    private fun changeCreatorIdToNickName(chat: Chat): String {
        var nickName = "Member"
//        Log.i("data", memberList.value.toString())
        memberList.value?.forEach { it ->
            if (chat.creator == it.userId) {
                nickName = it.nickName!!
            }
        }
        return nickName
    }

    fun postMessage(chat: Chat) {
        firebaseDataRepository.postChatMessage(chat)
    }
}

sealed class WhoseMessage {

    data class SelfMessage(val chat: Chat) : WhoseMessage()

    data class OthersMessage(val chat: Chat) : WhoseMessage()

}