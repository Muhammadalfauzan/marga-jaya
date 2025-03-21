package com.example.kamandanoe.core.domain.model

data class UpdateUserModel(
    val id: String? = null,
    val email: String,
    val name: String,
    val no_telp: String,
    val password: String? = null,
    val new_password: String? = null,
    val role: String? = null,
)