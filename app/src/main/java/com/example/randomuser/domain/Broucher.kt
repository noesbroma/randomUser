package com.example.randomuser.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class Broucher (
    @SerializedName("title") val title: String,
    @SerializedName("previewUrls") val previewUrls: Preview
): Serializable


data class Preview (
    @SerializedName("w375") val w375: String
): Serializable
