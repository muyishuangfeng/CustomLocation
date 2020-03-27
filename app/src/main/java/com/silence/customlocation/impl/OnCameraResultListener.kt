package com.silence.customlocation.impl

import android.net.Uri

/**
 * 拍照和从相册选择结果接口
 */
interface OnCameraResultListener {

    fun onCameraResult(uri: Uri?, path: String?)

    fun onAlbumResult(uri: Uri?, path: String?)
}