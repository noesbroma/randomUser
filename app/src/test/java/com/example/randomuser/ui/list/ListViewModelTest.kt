package com.example.randomuser.ui.list

import android.content.Context
import com.example.randomuser.data.room.UserDao
import com.example.randomuser.data.room.UserDb
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
//import androidx.test.core.app.ApplicationProvider


class ListViewModelTest {
    //var userDao: UserDao
    //val context = ApplicationProvider.getApplicationContext<Context>()


    @Before
    fun setUp() {
        var listViewModel: ListViewModel = ListViewModel()
        //userDao = UserDb.getInstance(context).userDao()
    }

    @Test
    fun deleteUser() {
        //Assert.assertNull("userDao debe ser not null", userDao)
    }
}