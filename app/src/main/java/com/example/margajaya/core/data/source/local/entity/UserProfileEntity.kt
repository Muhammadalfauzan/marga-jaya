package com.example.margajaya.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val email: String?,
    val role: String?,
    val noTelp: String?
)