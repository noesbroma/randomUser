package com.example.randomuser.ui.detail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.randomuser.domain.User


class DetailViewModel() : ViewModel() {
    lateinit var user: User


    fun fetchIntentData(arguments: Bundle) {
        user = arguments?.getSerializable("EXTRA_USER") as User
    }
}