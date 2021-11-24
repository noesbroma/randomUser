package com.example.randomuser.data.brochures

import retrofit2.http.GET
import retrofit2.http.Query

interface BrouchersApiService {
    @GET("brochure/list/popular")
    suspend fun getBrouchers(@Query("maxDistance") maxDistance: String,
                        @Query("limit") limit: String,
                        @Query("offset") offset: String,
                        @Query("lat") lat: String,
                        @Query("lng") lng:String): GetBrouchersResponse?
}