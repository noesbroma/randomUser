package com.example.randomuser

import android.text.Editable
import com.example.randomuser.data.room.UserRoom
import com.example.randomuser.domain.User

interface ListInterface {
    interface Model {

    }


    interface View {
        fun observeViewModel()
        fun setListeners()
    }


    interface ViewModel {
        fun getUsersFromDB()
        fun getUsersFromAPI(value: Int)
        fun insertUsersToDB(users: ArrayList<User>, loadMore: Boolean)
        fun deleteUser(user: UserRoom)
        fun loadMore(page: Int)
        fun filterResults(text: Editable)
    }
}