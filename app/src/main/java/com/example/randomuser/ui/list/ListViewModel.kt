package com.example.randomuser.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuser.data.list.GetUsersResult
import com.example.randomuser.data.list.UserRandomRepository
import com.example.randomuser.domain.User
import com.example.randomuser.ui.randomUserApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.room.Room
import com.example.randomuser.data.room.*


class ListViewModel(application: Application, private val repository: UserRandomRepository): AndroidViewModel(application) {
    val onLoadUsersEvent = MutableLiveData<ArrayList<UserRoom>>()
    val onLoadMoreEvent = MutableLiveData<ArrayList<UserRoom>>()
    val results = "10"
    //lateinit var dataStoreManager: DataStoreManager
    var deletedUsers = ArrayList<String>()
    private val context = getApplication<Application>().applicationContext
    private var values = ArrayList<String>()
    private var users = ArrayList<User>()
    private lateinit var people: List<UserRoom>
    val liveQuote = MutableLiveData<List<UserRoom>>()
    val userDao = UserDb.getInstance(context).userDao()

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

        //ClearDeletedUsers()

        users.forEach {
            var name = Name(it.name.first, it.name.last)
            var r = UserRoom(it.gender, name, it.email, it.phone, Picture(it.picture.large, it.picture.medium, it.picture.thumbnail), Location(
                Street(it.location.street.name, it.location.street.number), it.location.city, it.location.state), Registered(it.registered.date.toString(), it.registered.age))
            userRooms.add(r)
        }

        //ClearDeletedUsers(userRooms)

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


    fun ClearDeletedUsers(): ArrayList<User> {
        users.forEach{ user ->
            if (isDeletedUser(user.phone)) {
                var user = users.filter { it.phone == user.phone}
                users.drop(users.indexOf(user))
            }
        }

        return users
    }


    fun loadMore(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    users = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>
                    insertUsersToDB(users, true)
                    //onLoadMoreEvent.value = ClearDeletedUsers()
                }

                is GetUsersResult.Error -> {
                }
            }
        }
    }


    fun saveDeletedUser(phone: String) {
        val sharedPreferences = context.getSharedPreferences("shared preferences", 0)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(deletedUsers.add(phone))
        editor.putString("DELETED_USERS", json)
        editor.apply()

        /*viewModelScope.launch(Dispatchers.IO) {
            dataStorageManager.saveUserToPreferencesStore(deletedUser = DeletedUser(name = "nombre1",
                                email = "email1")
            )
        }*/
    }


    fun getDeletedUsers() {
        val sharedPreferences = context.getSharedPreferences("shared preferences", 0)
        val gson = Gson()
        val json = sharedPreferences.getString("DELETED_USERS", "")
        val type = object: TypeToken<ArrayList<String>>() {
        }.type

        values = if(json == null)
            ArrayList()
        else
            gson.fromJson(json, type)
    }

    fun setArrayPrefs(arrayName: String, array: ArrayList<String?>) {
        val prefs: SharedPreferences = context.getSharedPreferences("deleted_users_prefs", 0)
        val editor = prefs.edit()
        editor.putInt("deleted_phone_size", array.size)
        for (i in 0 until array.size) editor.putString("deleted_phone_", array[i])
        editor.apply()
    }

    fun setDeleted(phone: String?) {
        val prefs: SharedPreferences = context.getSharedPreferences("deleted_users_prefs", 0)
        val editor = prefs.edit()
        editor.putBoolean("deleted_phone_" + phone, true)
        editor.apply()
    }

    fun isDeletedUser(phone: String): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences("deleted_users_prefs", 0)

        return prefs.getBoolean("deleted_phone_" + phone, false)
    }

    fun getArrayPrefs(arrayName: String): ArrayList<String?>? {
        val prefs: SharedPreferences = context.getSharedPreferences("deleted_users_prefs", 0)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array: ArrayList<String?> = ArrayList(size)
        for (i in 0 until size) array.add(prefs.getString(arrayName + "_" + i, null))
        return array
    }
}