package com.weiting.tohealth.mygrouppage.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.weiting.tohealth.databinding.DialogCreateGroupBinding

class CreateGroupDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogCreateGroupBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels)
        dialog!!.window?.setLayout(width, width)

    }


}