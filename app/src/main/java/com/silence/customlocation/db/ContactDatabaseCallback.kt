package com.silence.customlocation.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.silence.customlocation.util.contact.ContactUtil
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 回调
 */
class ContactDatabaseCallback(context: Context, private val scope: CoroutineScope) :
    RoomDatabase.Callback() {

    private var mContext: Context = context


    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        ContactDataBase.INSTANCE.let {
            scope.launch {
                val contactDao = it?.contactDao()
                Observable.create<MutableList<Contact>> { subscriber ->
                    val mList = contactDao?.queryAllContact()
                    if (mList?.size == 0) {
                        val contactList = ContactUtil.searchAllContacts(mContext.applicationContext)
                        subscriber.onNext(contactList)
                        subscriber.onComplete()
                    }

                }.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(Consumer {
                        for (index in 0 until it.size) {
                            val model = Contact()
                            model.id = index
                            model.userName = it[index].userName
                            model.userPhone = it[index].userPhone
                            model.userRemark = it[index].userRemark
                            contactDao?.insertContact(model)
                        }
                    })
            }
        }


    }

}




