package com.weiting.tohealth.data

import com.google.firebase.Timestamp

data class Group(
    val id: String? = null,
    val groupName: String? = null,
    val member: List<Member> = listOf(),
    val notes: List<Note> = listOf(),
    val calenderItems: List<CalenderItem> = listOf()
)

data class Member(
    val id: String? = null,
    val userId : String? = null,
    val name: String? = null,
    val private: Int? = null
    )

data class Note(
    val id: String? = null,
    val title: String? = null,
    val content: String? = null,
    val editor : String? = null,
    val createTimestamp: Timestamp? = null
)

data class CalenderItem(
    val id: String? = null,
    val editor: String? = null,
    val content: String? = null,
    val date: Timestamp? = null,
    val createTime: Timestamp? = null,
    val result: Int? = null
)

