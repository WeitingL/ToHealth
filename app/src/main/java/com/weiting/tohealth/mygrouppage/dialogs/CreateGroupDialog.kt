package com.weiting.tohealth.mygrouppage.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.databinding.DialogCreateGroupBinding
import com.weiting.tohealth.factory.AddGroupViewModelFactory

class CreateGroupDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogCreateGroupBinding.inflate(layoutInflater, container, false)
        val factory = AddGroupViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            HowToAddGroup.CREATE
        )
        val viewModel = ViewModelProvider(this, factory).get(AddGroupViewModel::class.java)

        viewModel.newGroupId.observe(viewLifecycleOwner) {
            binding.tvCode.text = it
        }

        binding.btEnterName.setOnClickListener {
            when (binding.tilGroupName.editText?.text.isNullOrEmpty()) {
                true -> Toast.makeText(context, getString(R.string.namingForGroup), Toast.LENGTH_LONG).show()
                false -> {
                    viewModel.createGroup(
                        Group(
                            id = viewModel.newGroupId.value,
                            groupName = binding.tilGroupName.editText?.text.toString()
                        )
                    )
                    findNavController().navigate(NavigationDirections.actionGlobalMyGroupFragment())
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels)
        dialog!!.window?.setLayout(width, width)
    }


}