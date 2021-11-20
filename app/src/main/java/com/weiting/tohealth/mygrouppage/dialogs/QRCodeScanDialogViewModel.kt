package com.weiting.tohealth.mygrouppage.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QRCodeScanDialogViewModel : ViewModel() {

    private val _groupIdFromQRCode = MutableLiveData<String>()
    val groupIdFromQRCode: LiveData<String>
        get() = _groupIdFromQRCode

    fun getGroupId(groupId: String){
        viewModelScope.launch {
            _groupIdFromQRCode.value = groupId
        }
    }


}