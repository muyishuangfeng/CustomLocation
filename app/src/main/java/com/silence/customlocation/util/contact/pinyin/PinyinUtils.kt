package com.silence.customlocation.util.contact.pinyin

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination


object PinyinUtils {

    /**
     * 获得汉语拼音首字母
     *
     * @param chines 汉字
     * @return
     */
    fun getAlpha(chines: String): String {
        var pinyinName = ""
        val nameChar = chines.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.UPPERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (i in nameChar.indices) {
            if (nameChar[i].toInt() > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat)[0][0]
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    e.printStackTrace()
                }

            } else {
                pinyinName += nameChar[i]
            }
        }
        return pinyinName
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     *
     * @param inputString 汉字
     * @return
     */
    fun getPingYin(inputString: String?): String {
        val format = HanyuPinyinOutputFormat()
        format.caseType = HanyuPinyinCaseType.LOWERCASE
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        format.vCharType = HanyuPinyinVCharType.WITH_V
        var output = ""
        if (inputString != null && inputString.length > 0
                && "null" != inputString) {
            val input = inputString.trim { it <= ' ' }.toCharArray()
            try {
                for (i in input.indices) {
                    if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+".toRegex())) {
                        val temp = PinyinHelper.toHanyuPinyinStringArray(
                                input[i], format)
                        output += temp[0]
                    } else
                        output += Character.toString(input[i])
                }
            } catch (e: BadHanyuPinyinOutputFormatCombination) {
                e.printStackTrace()
            }

        } else {
            return "*"
        }
        return output
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    fun converterToFirstSpell(chines: String): String {
        var pinyinName = ""
        val nameChar = chines.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.UPPERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (i in nameChar.indices) {
            if (nameChar[i].toInt() > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat)[0][0]
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    e.printStackTrace()
                }

            } else {
                pinyinName += nameChar[i]
            }
        }
        return pinyinName
    }
}