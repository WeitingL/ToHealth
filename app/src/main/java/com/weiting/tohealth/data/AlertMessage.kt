package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

/*
    LogId can be itemId for dug exhausted.
 */

@Parcelize
data class AlertMessage(
    var id: String? = null,
    val userId: String? = null,
    val itemId: String? = null,
    val logId: String? = null,
    val result: Int? = null,
    val createdTime: Timestamp? = null,
    val alreadySend: MutableList<String> = mutableListOf()
) : Parcelable
