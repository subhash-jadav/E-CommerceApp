package com.example.easyshop.models

data class UserModel(
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val cartItems: Map<String, Long> = mapOf(),
    val userAddress: String = ""

)
