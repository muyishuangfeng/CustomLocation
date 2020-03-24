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
}