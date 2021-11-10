package com.weiting.tohealth.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    var id: String? = null,
    val groupName: String? = null,
    var member: List<Member> = listOf(),
    var notes: List<Note> = listOf(),
    var calenderItems: List<CalenderItem> = listOf()
) : Parcelable

@Parcelize
data class Member(
    var id: String? = null,
    val userId: String? = null,
    val name: String? = null,
    var nickName: String? = null,
    var private: Int? = null,
    var profilePhoto: String? = null
) : Parcelable

@Parcelize
data class Note(
    var id: String? = null,
    val title: String? = null,
    val content: String? = null,
    var editor: String? = null,
    val footer: Int? = null,
    val createdTime: Timestamp? = null
) : Parcelable

@Parcelize
data class CalenderItem(
    var id: String? = null,
    var editor: String? = null,
    val content: String? = null,
    val date: Timestamp? = null,
    val createdTime: Timestamp? = null,
    val result: Int? = null
):Parcelable

