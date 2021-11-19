package com.example.randomuser.data.list

import com.example.randomuser.data.room.UserRoom
import com.example.randomuser.domain.User
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetUsersResponse (
    @SerializedName("results") val results: ArrayList<User>
): Serializable