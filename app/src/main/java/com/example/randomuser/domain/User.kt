package com.example.randomuser.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (
    @SerializedName("gender") val gender: String
): Serializable