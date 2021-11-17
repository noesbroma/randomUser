package com.example.randomuser.data.list

import retrofit2.http.GET
import retrofit2.http.Query

interface UserRandomApiService {
    @GET("/")
    suspend fun getUsers(@Query("page") page: String,
                        @Query("results") results: String): GetUsersResponse?
}