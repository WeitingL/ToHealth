package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: String? = null,
    val userId: String? = null,
    val logId: String? = null,
    val result: Int? = null,
    val createTimestamp: Timestamp? = null
) : Parcelable
