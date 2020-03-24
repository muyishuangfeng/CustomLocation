package com.silence.customlocation.util.contact.pinyin

import com.silence.customlocation.db.Contact
import com.silence.customlocation.model.ContactBean


/**
 * 拼音排序
 */
class PinyinComparator : Comparator<Contact> {

    override fun compare(o1: Contact?, o2: Contact?): Int {
        //这里主要是用来对RecyclerView里面的数据根据ABCDEFG...来排序
        return if (o1?.sortLetters.equals("@")
                || o2?.sortLetters.equals("#")) {
            -1;
        } else if (o1?.sortLetters.equals("#")
                || o2?.sortLetters.equals("@")) {
            1;
        } else {
            o1?.sortLetters!!.compareTo(o2?.sortLetters!!);
        }
    }
}