package com.example.randomuser.data.brochures


sealed class GetBrouchersResult {
    data class Ok(val getBrouchersResponse: GetBrouchersResponse) : GetBrouchersResult()
    object Error: GetBrouchersResult()
}