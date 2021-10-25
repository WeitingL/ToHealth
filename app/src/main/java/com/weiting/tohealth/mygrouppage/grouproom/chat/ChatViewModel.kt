package com.weiting.tohealth.mygrouppage.grouproom.chat

import androidx.lifecycle.ViewModel
import com.weiting.tohealth.data.Chat
import com.weiting.tohealth.data.Group

class ChatViewModel: ViewModel() {




}

sealed class WhoseMessage{

    data class SelfMessage(val chat: Chat):WhoseMessage()

    data class OthersMessage(val chat: Chat):WhoseMessage()

}