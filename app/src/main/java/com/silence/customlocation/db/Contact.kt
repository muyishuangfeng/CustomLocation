package com.silence.customlocation.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 联系人
 */
@Entity(tableName = "Contact")
class Contact {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = 0
    @ColumnInfo(name = "userName")
    var userName: String? = ""
    @ColumnInfo(name = "userPhone")
    var userPhone: String? = ""
    @ColumnInfo(name = "userRemark")
    var userRemark: String? = ""
    //显示数据拼音的首字母
    var sortLetters: String? = null

    override fun toString(): String {
        return "Contact(id=$id, userName=$userName, userPhone=$userPhone, userRemark=$userRemark, sortLetters=$sortLetters)"
    }


}

/**
 * 用户表
 */
@Entity(tableName = "User")
class User {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid: Int? = 0
    @ColumnInfo(name = "userName")
    var userName: String? = ""
    @ColumnInfo(name = "userPhone")
    var userPhone: String? = ""
    @ColumnInfo(name = "userPass")
    var userPass: String? = ""
    @ColumnInfo(name = "userEmail")
    var userEmail: String? = ""
    @ColumnInfo(name = "userIconPath")
    var userIconPath: String? = ""
    @ColumnInfo(name = "appKey")
    var appKey: String? = ""
    @ColumnInfo(name = "userNickName")
    var userNickName: String? = ""

    override fun toString(): String {
        return "User(uid=$uid, userName=$userName, userPhone=$userPhone, userPass=$userPass, userEmail=$userEmail, userIconPath=$userIconPath, appKey=$appKey, userNickName=$userNickName)"
    }


}