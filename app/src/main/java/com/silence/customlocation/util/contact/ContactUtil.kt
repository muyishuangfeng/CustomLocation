@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.silence.customlocation.util.contact

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.silence.customlocation.db.Contact
import kotlin.collections.ArrayList
import com.silence.customlocation.util.contact.pinyin.PinyinComparator
import java.util.*
import com.silence.customlocation.util.contact.pinyin.PinyinUtils


object ContactUtil {

    /**
     * 查询所有联系人信息
     */
    private fun getAllContacts(context: Context): MutableList<Contact> {
        val mList = ArrayList<Contact>()
        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null,
            null, null, null, null
        )
        while (cursor!!.moveToNext()) {
            val mBean = Contact()
            val mContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val mName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            mBean.userName = mName
            val mPhoneCursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + "=" + mContactId, null, null
            )
            while (mPhoneCursor!!.moveToNext()) {
                var phone =
                    mPhoneCursor.getString(mPhoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phone = phone.replace("-", "")
                phone = phone.replace(" ", "")
                mBean.userPhone = phone
            }
            //获取联系人备注信息
            val remarkCursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Nickname.NAME),
                ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
                arrayOf(mContactId), null
            )
            if (remarkCursor!!.moveToFirst()) {
                do {
                    val remark = remarkCursor.getString(
                        remarkCursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME)
                    );
                    mBean.userRemark = remark
                } while (remarkCursor.moveToNext());
            }
            mList.add(mBean);
            //记得要把cursor给close掉
            mPhoneCursor.close();
            remarkCursor.close();
        }
        cursor.close();
        return filledData(mList)
    }

    /**
     * 查询所有联系人信息
     */
    fun searchAllContacts(context: Context): MutableList<Contact> {
        return getAllContacts(context)


    }

    /**
     * 根据联系人姓名查询电话
     */
    fun searchPhone(context: Context, name: String): MutableList<Contact> {
        val mList = ArrayList<Contact>()
        val mBean = Contact()
        val dataCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.Data.DATA1),
            ContactsContract.Data.DISPLAY_NAME + "= ? ",
            arrayOf(name), null
        )
        if (dataCursor!!.count > 0) {
            mBean.userName = name
            while (dataCursor.moveToNext()) {
                var phone =
                    dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1))
                phone = phone.replace("-", "")
                phone = phone.replace(" ", "")
                mBean.userPhone = phone
            }
            mList.add(mBean)
            dataCursor.close()
        }
        return mList
    }

    /**
     * 按拼音排序
     */
    private fun filledData(date: MutableList<Contact>): MutableList<Contact> {
        val mSortList = ArrayList<Contact>()
        for (i in date.indices) {
            val sortModel = Contact()
            sortModel.userName = date[i].userName
            sortModel.userPhone = date[i].userPhone
            val pinyin = PinyinUtils.converterToFirstSpell(date[i].userName!!)
            sortModel.sortLetters = pinyin
            mSortList.add(sortModel)
        }
        Collections.sort(mSortList, PinyinComparator())
        Log.e("TAG", mSortList.toString())
        return mSortList
    }
}
