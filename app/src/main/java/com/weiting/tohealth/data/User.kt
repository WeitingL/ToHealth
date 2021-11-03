package com.weiting.tohealth.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name : String? = null,
    val id : String? = null,
    val groupList: List<String> = listOf()
): Parcelable