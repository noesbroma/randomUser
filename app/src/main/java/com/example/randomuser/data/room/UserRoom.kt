package com.example.randomuser.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserRoom (
    var gender: String,
    @Embedded var name: Name,
    var email: String,
    var phone: String,
    @Embedded var picture: Picture,
    @Embedded var location: Location,
    @Embedded var registered: Registered
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

data class Name (
    var title: String,
    var first: String,
    var last: String
    )

data class Picture (
    var large: String,
    var medium: String,
    var thumbnail: String
)

data class Location (
    @Embedded var street: Street,
    var city: String,
    var state: String
    )

data class Street (
    var number: String,
    var name: String
)

data class Registered (
    var date: String,
    var age: String
)