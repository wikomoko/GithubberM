package com.example.githubberm.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowingFragmentUserDatas(
    val userLogin: String,
    val userName: String,
    val userAvatar: String,
    val userCompany: String,
    val userLocation: String,
    val userRepository: String,
    val userFollowers: String,
    val userFollowing: String,
) : Parcelable