package com.silence.customlocation.util.glide

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by Silence on 2018/1/5.
 */
object GlideUtils {

    /**
     * 通过uri加载图片
     */
    fun loadUri(activity: Activity, uri: Uri?, img: ImageView) {
        Glide.with(activity).load(uri).into(img)
    }

    /**
     * 通过文件路径加载图片
     */
    fun loadPath(activity: Activity, filePath: String?, img: ImageView) {
        Glide.with(activity).load(filePath).into(img)
    }
}