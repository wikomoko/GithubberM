package com.example.githubberm.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Person(
    @PrimaryKey
    val userLogin: String,
    val userName: String,
    val userAvatar: String,
    val userCompany: String,
    val userLocation: String,
    val userRepository: String,
    val userFollowers: String,
    val userFollowing: String,
):Parcelable

