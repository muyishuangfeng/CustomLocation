package com.silence.customlocation.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel
 */
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    //所有联系人
    val allContacts: LiveData<MutableList<Contact>>

    init {
        val contactDao = ContactDataBase.getDataBase(application,viewModelScope).contactDao()
        repository = ContactRepository(contactDao)
        allContacts = repository.allContacts
    }


    fun insert(contactBean: Contact) = viewModelScope.launch {
        repository.insert(contactBean)
    }


}