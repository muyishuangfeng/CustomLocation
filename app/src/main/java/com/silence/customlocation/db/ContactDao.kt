package com.silence.customlocation.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {

    @Insert
    fun insertContact(contact: Contact)

    @Query("SELECT * FROM Contact")
    fun queryContact(): LiveData<MutableList<Contact>>

    @Query("SELECT * FROM Contact")
    fun queryAllContact(): MutableList<Contact>

}