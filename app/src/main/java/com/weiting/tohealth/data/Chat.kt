package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val id: String? = null,
    val groupId: String? = null,
    val context: String? = null,
    val creator: String? = null,
    val createTimestamp: Timestamp? = null
):Parcelable