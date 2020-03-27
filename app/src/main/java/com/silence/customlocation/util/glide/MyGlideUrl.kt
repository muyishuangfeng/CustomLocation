package com.silence.customlocation.util.glide

import com.bumptech.glide.load.model.GlideUrl

/**
 * Created by Silence on 2018/1/5.
 */
class MyGlideUrl internal constructor(private val mUrl: String) : GlideUrl(mUrl) {

    override fun getCacheKey(): String {
        return mUrl.replace(findTokenParam(), "")
    }

    private fun findTokenParam(): String {
        var tokenParam = ""
        val tokenKeyIndex =
            if (mUrl.contains("?token=")) mUrl.indexOf("?token=") else mUrl.indexOf("&token=")
        if (tokenKeyIndex != -1) {
            val nextAndIndex = mUrl.indexOf("&", tokenKeyIndex + 1)
            tokenParam = if (nextAndIndex != -1) {
                mUrl.substring(tokenKeyIndex + 1, nextAndIndex + 1)
            } else {
                mUrl.substring(tokenKeyIndex)
            }
        }
        return tokenParam
    }

}