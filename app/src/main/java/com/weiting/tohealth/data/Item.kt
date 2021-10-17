package com.weiting.tohealth.data

data class Drug(
    val drug : String,
    val unit : String,
    val time : String,
    val stock : String
)

data class Group(
    val name: String,
    val code: String,
    val member: List<Member>,
    val note: List<Note>
)

data class Member(
    val name: String
)

data class Note(
    val title: String,
    val content: String
)