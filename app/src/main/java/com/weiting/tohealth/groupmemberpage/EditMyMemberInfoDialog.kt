package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.DialogEditMyNicknameBinding
import com.weiting.tohealth.factory.GroupMemberEditViewModelFactory

class EditMyMemberInfoDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogEditMyNicknameBinding.inflate(inflater, container, false)
        val memberInfo = EditMyMemberInfoDialogArgs.fromBundle(requireArguments()).memberInfo
        val groupId = EditMyMemberInfoDialogArgs.fromBundle(requireArguments()).groupId
        val factory = GroupMemberEditViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            memberInfo,
            groupId
        )
        val viewModel = ViewModelProvider(this, factory).get(GroupMemberEditViewModel::class.java)

        binding.edtMyNickName.hint = memberInfo.nickName

        binding.btChange.setOnClickListener {

            if (binding.edtMyNickName.editableText.isNullOrEmpty()) {
                memberInfo.nickName = memberInfo.name
            } else {
                memberInfo.nickName = binding.edtMyNickName.text.toString()
            }

            viewModel.postNewNickName(memberInfo)
            Toast.makeText(context, getString(R.string.upDateSuccess), Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.private_all -> viewModel.getNewPrivateSet(0)
                R.id.private_readLog -> viewModel.getNewPrivateSet(1)
                R.id.private_onlyRead -> viewModel.getNewPrivateSet(2)
                R.id.private_allRefused -> viewModel.getNewPrivateSet(3)
            }
        }

        when (memberInfo.private) {
            0 -> binding.privateAll.isChecked = true
            1 -> binding.privateReadLog.isChecked = true
            2 -> binding.privateOnlyRead.isChecked = true
            3 -> binding.privateAllRefused.isChecked = true
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.3).toInt()
        dialog!!.window?.setLayout(width * 3, width * 4)
    }
}
