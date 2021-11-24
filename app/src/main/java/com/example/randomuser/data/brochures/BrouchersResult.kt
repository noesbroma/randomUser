package com.example.randomuser.data.brochures

import com.example.randomuser.domain.Broucher
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class BrouchersResult (
    @SerializedName("brochures") val brochures: ArrayList<Broucher>
): Serializable

