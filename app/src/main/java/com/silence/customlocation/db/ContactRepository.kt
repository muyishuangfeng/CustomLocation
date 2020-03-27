package com.silence.customlocation.db

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts = contactDao.queryContact()


    suspend fun insert(contactBean: Contact) {
        contactDao.insertContact(contactBean)
    }

    suspend fun insert(user: User) {
        contactDao.insertUser(user)
    }


}