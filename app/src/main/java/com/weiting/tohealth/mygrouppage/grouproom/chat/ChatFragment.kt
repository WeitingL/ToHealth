package com.weiting.tohealth.mygrouppage.grouproom.chat

import android.os.Bundle
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
import com.weiting.tohealth.mygrouppage.grouproom.GROUP

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ChatroomFragmentBinding.inflate(inflater, container, false)
        val group: Group = arguments?.get(GROUP) as Group

        val factory =
            ChatViewModelFactory(PublicApplication.application.firebaseDataRepository, group)
        val viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        val adapter = ChatAdapter()

        viewModel.chatMediatorLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rvChats.smoothScrollToPosition(adapter.itemCount - 1)
        }

        binding.ibSentMessage.setOnClickListener {
            if (binding.etvMessage.text.isNotEmpty()) {
                viewModel.postMessage(
                    Chat(
                        groupId = group.id,
                        context = binding.etvMessage.text.toString(),
                        creator = UserManager.UserInfo.id,
                        createdTime = Timestamp.now()
                    )
                )
            }
            binding.etvMessage.text.clear()
        }

        binding.rvChats.adapter = adapter
        return binding.root
    }
}
