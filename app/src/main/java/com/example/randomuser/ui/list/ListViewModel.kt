package com.example.randomuser.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.list.GetUsersResult
import com.example.randomuser.data.list.UserRandomRepository
import com.example.randomuser.domain.User
import kotlinx.coroutines.launch
import android.util.Log
import com.example.randomuser.data.room.*


class ListViewModel(application: Application, private val repository: UserRandomRepository): AndroidViewModel(application) {
    val onLoadUsersEvent = MutableLiveData<ArrayList<UserRoom>>()
    val onLoadMoreEvent = MutableLiveData<ArrayList<UserRoom>>()
    val results = "10"
    private val context = getApplication<Application>().applicationContext
    private var users = ArrayList<User>()
    val liveQuote = MutableLiveData<List<UserRoom>>()
    private val userDao = UserDb.getInstance(context).userDao()

    fun fetchRandomQuote() {
        viewModelScope.launch {
            var userRooms: List<UserRoom> = ArrayList()

            try {
                userRooms = userDao.getAll()
            } catch(e: java.lang.Exception) {
                Log.e("Room", "onCreate: ", e)
            }

            liveQuote.postValue(userRooms)
        }
    }


    fun getUsers(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    users = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>
                    insertUsersToDB(users, false)
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }


    fun insertUsersToDB(users: ArrayList<User>, loadMore: Boolean) {
        var userRooms: ArrayList<UserRoom> = ArrayList<UserRoom>()

        users.forEach {
            var r = UserRoom(it.gender, Name(it.name.title, it.name.first, it.name.last), it.email, it.phone, Picture(it.picture.large, it.picture.medium, it.picture.thumbnail), Location(
                Street(it.location.street.name, it.location.street.number), it.location.city, it.location.state), Registered(it.registered.date.toString(), it.registered.age))

            userRooms.add(r)
        }

        try {
            userDao.insert(userRooms)
        } catch (e: Exception){
            Log.e("Room", "onCreate: ", e)
        }

        if (loadMore) {
            onLoadMoreEvent.value = userRooms
        } else {
            onLoadUsersEvent.value = userRooms
        }
    }


    fun updateUser(user: UserRoom) {
        try {
            userDao.deleteByUserEmail(user.email)
        } catch (e: Exception){
            Log.e("Room", "updateUser: ", e)
        }
    }


    fun loadMore(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    users = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>

                    insertUsersToDB(users, true)
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }
}