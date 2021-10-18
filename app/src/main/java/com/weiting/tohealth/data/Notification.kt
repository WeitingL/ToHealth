package com.weiting.tohealth.data

import com.google.firebase.Timestamp

data class Notification(
    val id: String? = null,
    val userId: String? = null,
    val logId: String? = null,
    val result: Int? = null,
    val createTimestamp: Timestamp? = null
)
