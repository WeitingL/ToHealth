package com.weiting.tohealth.mygrouppage.grouproom.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.Chat
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.ChatroomFragmentBinding
import com.weiting.tohealth.factory.ChatViewModelFactory

class ChatFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ChatroomFragmentBinding.inflate(inflater, container, false)
        val group: Group = arguments?.get("group") as Group
        val factory =
            ChatViewModelFactory(PublicApplication.application.firebaseDataRepository, group)
        val viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        val adapter = ChatAdapter()

        viewModel.chatList.observe(viewLifecycleOwner){
            viewModel.identifyUser(it)
        }

        viewModel.chatMessages.observe(viewLifecycleOwner){
            Log.i("CalenderItemList", it.toString())
            adapter.submitList(it)
        }

        binding.ibSentMessage.setOnClickListener {
            viewModel.postMessage(Chat(
                groupId = group.id,
                context = binding.etvMessage.text.toString(),
                creator = UserManager.userId,
                createTimestamp = Timestamp.now()
            ))
        }

        binding.rvChats.adapter = adapter
        return binding.root
    }

}