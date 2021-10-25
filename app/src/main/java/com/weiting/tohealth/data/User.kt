package com.weiting.tohealth.data

data class User(
    val name : String? = null,
    val id : String? = null,
    val groupList: List<String> = listOf()
)