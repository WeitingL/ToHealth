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
    val userId: String? = null,
    val nickName: String? = null,
    val private: Int? = null
) : Parcelable

@Parcelize
data class Note(
    val id: String? = null,
    val title: String? = null,
    val content: String? = null,
    val editor: String? = null,
    val footer: Int? = null,
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

//This is firebase form.
data class Relationships(
    var id: String? = null,
    val userId: String? = null,
    val groupId:String? = null,
    val nickName: String? = null,
    val private: Int? = null
)

