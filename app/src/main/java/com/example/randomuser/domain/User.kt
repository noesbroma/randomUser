package com.example.randomuser.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (
    @SerializedName("name") val name: Name,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("picture") val picture: Picture
): Serializable


data class Name (
    @SerializedName("title") val title: String,
    @SerializedName("first") val first: String,
    @SerializedName("last") val last: String,
): Serializable


data class Picture (
    @SerializedName("large") val large: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("thumbnail") val thumbnail: String,
): Serializable