package com.silence.customlocation.db

import com.silence.customlocation.model.ContactBean

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts = contactDao.queryContact()

    suspend fun insert(contactBean: Contact) {
        contactDao.insertContact(contactBean)
    }


}