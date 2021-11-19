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





class ListViewModel(application: Application, private val repository: UserRandomRepository): AndroidViewModel(application) {
    val onLoadUsersEvent = MutableLiveData<ArrayList<User>>()
    val onLoadMoreEvent = MutableLiveData<ArrayList<User>>()
    val results = "10"
    //lateinit var dataStoreManager: DataStoreManager
    var deletedUsers = ArrayList<String>()
    private val context = getApplication<Application>().applicationContext
    private var values = ArrayList<String>()
    private var users = ArrayList<User>()


    fun getUsers(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getUsers(page.toString(), results)) {
                is GetUsersResult.Ok -> {
                    users = result.getUsersResponse.results.distinctBy { it.name } as ArrayList<User>

                    //ClearDeletedUsers()

                    onLoadUsersEvent.value = ClearDeletedUsers()
                }

                is GetUsersResult.Error -> {
                }
            }
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
                    onLoadMoreEvent.value = ClearDeletedUsers()
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