package com.example.randomuser.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.list.GetUsersResult
import com.example.randomuser.data.list.UserRandomRepository
import kotlinx.coroutines.launch

class ListViewModel(private val repository: UserRandomRepository): ViewModel() {
    fun getUsers() {
        viewModelScope.launch {
            when (val result = repository.getUsers("40")) {
                is GetUsersResult.Ok -> {

                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }
}