package com.example.randomuser.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class User (
    @SerializedName("gender") val gender: String,
    @SerializedName("name") val name: Name,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("picture") val picture: Picture,
    @SerializedName("location") val location: Location,
    @SerializedName("registered") val registered: Registered,
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


data class Location (
    @SerializedName("street") val street: Street,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String
): Serializable


data class Street (
    @SerializedName("number") val number: String,
    @SerializedName("name") val name: String
): Serializable


data class Registered (
    @SerializedName("date") val date: Date,
    @SerializedName("age") val age: String
): Serializable