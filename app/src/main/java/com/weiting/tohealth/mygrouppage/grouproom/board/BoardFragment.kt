package com.weiting.tohealth.mygrouppage.grouproom.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.databinding.BroadFragmentBinding
import com.weiting.tohealth.factory.BoardViewModelFactory
import com.weiting.tohealth.mygrouppage.grouproom.GROUP

class BoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BroadFragmentBinding.inflate(inflater, container, false)
        val group: Group = arguments?.get(GROUP) as Group
        val factory =
            BoardViewModelFactory(PublicApplication.application.firebaseDataRepository, group)
        val viewModel = ViewModelProvider(this, factory).get(BoardViewModel::class.java)
        val adapter = BoardAdapter(viewModel)

        viewModel.boardLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)

            if (it.isNotEmpty()) {
                binding.apply {
                    lavNote.visibility = View.GONE
                    tvNothingToShow.visibility = View.GONE
                }
            } else {
                binding.apply {
                    lavNote.visibility = View.VISIBLE
                    tvNothingToShow.visibility = View.VISIBLE
                }
            }
        }

        binding.fbAddNewMessage.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalEditNoteAndCalenderItemFragment(
                    group
                )
            )
        }

        binding.rvBoard.adapter = adapter
        return binding.root
    }
}
