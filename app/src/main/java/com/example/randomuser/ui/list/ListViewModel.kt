package com.example.randomuser.ui.list

import android.app.Application
import android.text.Editable
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
    val onFilteredUsersEvent = MutableLiveData<ArrayList<UserRoom>>()
    var usersRoom = ArrayList<UserRoom>()
    var filteredUsersRoom = ArrayList<UserRoom>()

    private val context = getApplication<Application>().applicationContext
    private var users = ArrayList<User>()
    private val userDao = UserDb.getInstance(context).userDao()
    private val results = "10"


    fun getUsersFromDB() {
        viewModelScope.launch {
            try {
                usersRoom = userDao.getAll() as ArrayList<UserRoom>
                usersRoom = usersRoom.distinctBy { it.phone } as ArrayList<UserRoom>
            } catch(e: java.lang.Exception) {
                Log.e("Room", "onCreate: ", e)
            }

            onLoadUsersEvent.postValue(usersRoom)
        }
    }


    fun getUsersFromAPI(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    users = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>
                    insertUsersToDB(users, false)
                    onLoadUsersEvent.value = usersRoom
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }


    fun insertUsersToDB(users: ArrayList<User>, loadMore: Boolean) {
        users.forEach {
            var r = UserRoom(it.gender, Name(it.name.title, it.name.first, it.name.last), it.email, it.phone, Picture(it.picture.large, it.picture.medium, it.picture.thumbnail), Location(
                Street(it.location.street.name, it.location.street.number), it.location.city, it.location.state), Registered(it.registered.date.toString(), it.registered.age))

            usersRoom.add(r)
            usersRoom = usersRoom.distinctBy { it.phone } as ArrayList<UserRoom>
        }

        try {
            userDao.insert(usersRoom)
        } catch (e: Exception){
            Log.e("Room", "onCreate: ", e)
        }

        if (loadMore) {
            onLoadMoreEvent.value = usersRoom
        } else {
            onLoadUsersEvent.value = usersRoom
        }
    }


    fun deleteUser(user: UserRoom) {
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
                    users = result.getUsersResponse.results.distinctBy { it.phone } as ArrayList<User>

                    insertUsersToDB(users, true)
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }


    fun filterResults(text: Editable) {
        viewModelScope.launch {
            try {
                if (text.toString().isEmpty()) {
                    onFilteredUsersEvent.value = usersRoom
                } else {
                    filteredUsersRoom = usersRoom.filter{it.email.contains(text.toString()) || it.name.first.contains(text.toString()) || it.name.last.contains(text.toString())} as ArrayList<UserRoom>
                    onFilteredUsersEvent.value = filteredUsersRoom
                }
            } catch (e: Exception) {
                Log.e("Room", "filterResults: ", e)
            }
        }
    }
}