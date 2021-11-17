package com.example.randomuser.data.list

import com.example.randomuser.domain.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserRandomApiService {
    @GET("/")
    suspend fun getUsers(@Query("results") results: String): GetUsersResponse?
}