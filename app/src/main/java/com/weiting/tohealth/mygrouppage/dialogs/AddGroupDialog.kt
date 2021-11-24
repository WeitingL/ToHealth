package com.weiting.tohealth.mygrouppage.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.databinding.DialogAddGroupBinding

class AddGroupDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAddGroupBinding.inflate(layoutInflater, container, false)

        binding.apply {
            btNewGroup.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalCreateGroupDialog())
            }

            btAddGroup.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalJoinGroupDialog(null))
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
