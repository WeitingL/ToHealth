package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    var id: String? = null,
    val groupId: String? = null,
    val context: String? = null,
    var creator: String? = null,
    val createdTime: Timestamp? = null
):Parcelable