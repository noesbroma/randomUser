package com.example.randomuser.data.room

import android.provider.ContactsContract
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * from UserRoom")
    suspend fun getAll(): List<UserRoom>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: List<UserRoom>)

    @Update
    fun update(user: UserRoom)

    /*@Query("UPDATE UserRoom SET isDeleted = :isDeleted WHERE phone = :phone")
    fun deleteUser(phone: String, isDeleted: Boolean?): Int*/

    @Delete
    fun delete(user: UserRoom)

    @Query("DELETE FROM UserRoom WHERE email = :email")
    fun deleteByUserEmail(email: String)
}