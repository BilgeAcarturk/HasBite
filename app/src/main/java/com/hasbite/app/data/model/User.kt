package com.hasbite.app.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val bio: String = "",
    val avatar: String = "avatar1",
    val privateAccount: Boolean = false
)