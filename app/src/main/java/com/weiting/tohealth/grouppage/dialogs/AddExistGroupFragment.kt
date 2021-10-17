package com.weiting.tohealth.grouppage.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.weiting.tohealth.databinding.DialogAddExistGroupBinding
import com.weiting.tohealth.databinding.DialogNewGroupBinding

class AddExistGroupFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAddExistGroupBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        dialog!!.window?.setLayout(width * 2, width)

    }
    
}