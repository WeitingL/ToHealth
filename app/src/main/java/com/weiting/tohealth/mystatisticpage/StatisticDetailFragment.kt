package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.User
import com.weiting.tohealth.databinding.MystatisticItemFagmentBinding
import com.weiting.tohealth.factory.StatisticDetailViewModelFactory
import com.weiting.tohealth.mymanagepage.ITEM_TYPE
import com.weiting.tohealth.mymanagepage.USER

class StatisticDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MystatisticItemFagmentBinding.inflate(inflater, container, false)

        // Get the type from MyStatisticAdapter.
        val statisticType = arguments?.get(ITEM_TYPE) as ItemType
        val user = arguments?.get(USER) as User
        val factory =
            StatisticDetailViewModelFactory(
                PublicApplication.application.firebaseDataRepository,
                user.id?:"",
                statisticType
            )
        val viewModel = ViewModelProvider(this, factory).get(StatisticDetailViewModel::class.java)
        val adapter = StatisticDetailAdapter()

        viewModel.logList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
                binding.lavEmptyList.visibility = View.GONE
                binding.tvEmptyList.visibility = View.GONE
            } else {
                binding.lavEmptyList.visibility = View.VISIBLE
                binding.lavEmptyList.setAnimation(R.raw.filling_list)
                binding.tvEmptyList.visibility = View.VISIBLE
            }
        }

        binding.rvStatisticDataList.adapter = adapter
        return binding.root
    }
}
