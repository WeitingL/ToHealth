package com.weiting.tohealth.data

import com.google.firebase.Timestamp

data class Chat(
    val id: String? = null,
    val groupId: String? = null,
    val context: String? = null,
    val creator: String? = null,
    val createTimestamp: Timestamp? = null
    )