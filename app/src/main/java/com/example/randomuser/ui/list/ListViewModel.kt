package com.example.randomuser.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.list.GetUsersResult
import com.example.randomuser.data.list.UserRandomRepository
import com.example.randomuser.domain.User
import kotlinx.coroutines.launch


class ListViewModel(private val repository: UserRandomRepository): ViewModel() {
    val onLoadUsersEvent = MutableLiveData<ArrayList<User>>()
    val onLoadMoreEvent = MutableLiveData<ArrayList<User>>()
    val results = "10"


    fun getUsers(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    onLoadUsersEvent.value = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }


    fun loadMore(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    onLoadMoreEvent.value = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }
}