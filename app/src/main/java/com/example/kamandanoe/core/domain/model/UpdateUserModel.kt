package com.example.kamandanoe.core.domain.model

data class UpdateUserModel(
    val email: String,
    val name: String,
    val no_telp: String,
    val password: String,
    val new_password: String
)