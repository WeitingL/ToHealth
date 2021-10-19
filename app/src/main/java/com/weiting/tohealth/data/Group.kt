package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: String? = null,
    val groupName: String? = null,
    val member: List<Member> = listOf(),
    val notes: List<Note> = listOf(),
    val calenderItems: List<CalenderItem> = listOf()
) : Parcelable

@Parcelize
data class Member(
    val id: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val private: Int? = null
) : Parcelable

@Parcelize
data class Note(
    val id: String? = null,
    val title: String? = null,
    val content: String? = null,
    val editor: String? = null,
    val createTimestamp: Timestamp? = null
) : Parcelable

@Parcelize
data class CalenderItem(
    val id: String? = null,
    val editor: String? = null,
    val content: String? = null,
    val date: Timestamp? = null,
    val createTime: Timestamp? = null,
    val result: Int? = null
):Parcelable

