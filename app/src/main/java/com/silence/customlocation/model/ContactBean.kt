package com.silence.customlocation.model

class ContactBean {

    var mName: String? = null
    var mPhone: String? = null
    var mRemark: String? = null
    var sortLetters: String? = null//显示数据拼音的首字母


    override fun toString(): String {
        return "ContactBean(mName=$mName, mPhone=$mPhone, mRemark=$mRemark)"
    }


}