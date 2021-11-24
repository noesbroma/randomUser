package com.example.randomuser.data.brochures

import com.example.randomuser.domain.Broucher
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetBrouchersResponse (
    @SerializedName("brochures") val brochuresList: ArrayList<Broucher>
): Serializable