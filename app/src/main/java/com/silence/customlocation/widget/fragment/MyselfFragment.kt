package com.silence.customlocation.widget.fragment

import android.content.Intent
import android.net.Uri
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseFragment
import com.silence.customlocation.impl.IDialogResultListener
import com.silence.customlocation.impl.OnCameraResultListener
import com.silence.customlocation.ui.dialog.DialogFragmentHelper
import com.silence.customlocation.util.CameraUtil
import com.silence.customlocation.util.glide.GlideUtils
import kotlinx.android.synthetic.main.fragment_myself.*


class MyselfFragment : BaseFragment() {



    override fun getLayoutID(): Int {
        return R.layout.fragment_myself
    }

    override fun initView() {

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        btn_load.setOnClickListener {
            DialogFragmentHelper.showBottomDialog(
                childFragmentManager,
                object : IDialogResultListener<Int> {
                    override fun onDataResult(result: Int) {
                        when (result) {
                            0 -> {//相机
                                CameraUtil.openCamera(mActivity!!)
                            }
                            1 -> {//相册
                                CameraUtil.choosePhoto(mActivity!!)
                            }

                        }
                    }

                },
                true
            )
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CameraUtil.onActivityResult(
            mActivity!!,
            requestCode,
            resultCode,
            data,
            object : OnCameraResultListener {
                override fun onCameraResult(uri: Uri?, path: String?) {
                    if (uri != null) {
                        GlideUtils.loadUri(mActivity!!, uri, img_avatar)
                    }
                    if (path != null) {
                        GlideUtils.loadPath(mActivity!!, path, img_avatar)
                    }
                }

                override fun onAlbumResult(uri: Uri?, path: String?) {
                    if (uri != null) {
                        GlideUtils.loadUri(mActivity!!, uri, img_avatar)
                    }
                    if (path != null) {
                        GlideUtils.loadPath(mActivity!!, path, img_avatar)
                    }
                }

            })
    }



}