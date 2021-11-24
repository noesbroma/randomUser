package com.example.randomuser.data.brochures

import com.example.randomuser.data.list.GetUsersResult
import com.example.randomuser.data.list.UserRandomApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BrouchersRepository(val brouchersApiService: BrouchersApiService) {
    suspend fun getBrouchers(maxDistance: String, limit: String, offset: String, lat: String, lng: String): GetBrouchersResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = brouchersApiService.getBrouchers(maxDistance, limit, offset, lat, lng)

                if (response != null) {
                    GetBrouchersResult.Ok(response)
                } else {
                    GetBrouchersResult.Error
                }
            } catch (e: Throwable) {
                GetBrouchersResult.Error
            }
        }
    }
}