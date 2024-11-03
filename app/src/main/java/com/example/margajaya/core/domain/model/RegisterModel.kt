package com.example.margajaya.core.domain.model

data class RegisterModel (
    val name : String,
    val email : String,
    val password : String,
    val no_telp : String,
    val confirm_password : String
)