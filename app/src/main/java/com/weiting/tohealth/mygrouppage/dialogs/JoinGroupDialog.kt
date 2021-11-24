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
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.DialogJoinGroupBinding
import com.weiting.tohealth.factory.AddGroupViewModelFactory

class JoinGroupDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogJoinGroupBinding.inflate(layoutInflater, container, false)
        val factory = AddGroupViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            HowToAddGroup.JOIN
        )
        val groupId = JoinGroupDialogArgs.fromBundle(requireArguments()).groupId
        val viewModel = ViewModelProvider(this, factory).get(AddGroupViewModel::class.java)

        if (groupId != null) {
            binding.tilGroupName.editText?.setText(groupId)
        }

        binding.imScanQR.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalQRCodeScanDialog())
        }

        binding.btEnterCode.setOnClickListener {
            viewModel.checkIsGroupIdExist(binding.tilGroupName.editText?.text.toString())
        }

        viewModel.isGroupExist.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    viewModel.checkIsRelationshipExist(
                        userId = UserManager.UserInformation.id ?: "",
                        groupId = binding.tilGroupName.editText?.text.toString()
                    )
                }

                false -> {
                    Toast.makeText(context, getString(R.string.checkTheGroupId), Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.isRelationshipExist.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    Toast.makeText(context, getString(R.string.alreadyHere), Toast.LENGTH_LONG).show()
                }

                false -> {
                    viewModel.joinGroup(
                        member = Member(
                            userId = UserManager.UserInformation.id ?: "",
                            private = 0,
                            name = UserManager.UserInformation.name,
                            nickName = UserManager.UserInformation.name
                        ),
                        groupId = binding.tilGroupName.editText?.text.toString()
                    )
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        dialog!!.window?.setLayout(width * 2, width)
    }
}
