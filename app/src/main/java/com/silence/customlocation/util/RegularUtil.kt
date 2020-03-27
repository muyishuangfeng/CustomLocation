package com.silence.customlocation.util

import java.util.regex.Pattern

object RegularUtil {
    /**
     * 是否包含中文
     */
    fun isContainChinese(str: String?): Boolean {
        val pattern = Pattern.compile("[\u4e00-\u9fa5]")
        val matcher = pattern.matcher(str)
        return matcher.find()
    }

    /**
     * 是否以字母或数字开始
     */
    fun whatStartWith(str: String?): Boolean {
        val pattern = Pattern.compile("^([A-Za-z]|[0-9])")
        val matcher = pattern.matcher(str)
        return matcher.find()
    }

    /**
     * 是否包含字母数字
     */
    fun whatContain(str: String?): Boolean {
        val pattern =
            Pattern.compile("^[0-9a-zA-Z][a-zA-Z0-9_\\-@.]{3,127}$")
        val matcher = pattern.matcher(str)
        return matcher.find()
    }
}