package com.example.randomuser.data.list

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRandomRepository(val userRandomApiService: UserRandomApiService) {
    suspend fun getUsers(results: String): GetUsersResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = userRandomApiService.getUsers(results)

                if (response != null) {
                    GetUsersResult.Ok(response)
                } else {
                    GetUsersResult.Error
                }
            } catch (e: Throwable) {
                GetUsersResult.Error
            }
        }
    }
}