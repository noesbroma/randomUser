package com.example.randomuser.data.list


sealed class GetUsersResult {
    data class Ok(val getUsersResponse: GetUsersResponse) : GetUsersResult()
    object Error: GetUsersResult()
}