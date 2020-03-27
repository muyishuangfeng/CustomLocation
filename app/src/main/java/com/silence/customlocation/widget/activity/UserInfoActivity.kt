package com.silence.customlocation.widget.activity

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseActivity
import com.silence.customlocation.common.Constants
import com.silence.customlocation.db.ContactViewModel
import com.silence.customlocation.db.User
import com.silence.customlocation.impl.IDialogResultListener
import com.silence.customlocation.impl.OnCameraResultListener
import com.silence.customlocation.ui.dialog.DialogFragmentHelper
import com.silence.customlocation.util.CameraUtil
import com.silence.customlocation.util.SPUTil
import com.silence.customlocation.util.glide.GlideUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File

class UserInfoActivity : BaseActivity() {

    private lateinit var viewModel: ContactViewModel
    //用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private var mCameraPath: String? = null


    override fun getLayoutID(): Int {
        return R.layout.activity_user_info
    }

    override fun initData() {
        SPUTil.init(this)
        img_user_icon.setOnClickListener {
            DialogFragmentHelper.showBottomDialog(
                supportFragmentManager,
                object : IDialogResultListener<Int> {
                    override fun onDataResult(result: Int) {
                        when (result) {
                            0 -> {//相机
                                CameraUtil.openCamera(this@UserInfoActivity)
                            }
                            1 -> {//相册
                                CameraUtil.choosePhoto(this@UserInfoActivity)
                            }

                        }
                    }

                },
                true
            )

        }
        btn_finish.setOnClickListener {
            end()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CameraUtil.onActivityResult(
            this,
            requestCode,
            resultCode,
            data,
            object : OnCameraResultListener {
                override fun onCameraResult(uri: Uri?, path: String?) {
                    if (uri != null) {
                        GlideUtils.loadUri(this@UserInfoActivity, uri, img_user_icon)
                        mCameraPath = CameraUtil.getRealFilePath(this@UserInfoActivity, uri)
                    }
                    if (path != null) {
                        mCameraPath = path
                        GlideUtils.loadPath(this@UserInfoActivity, path, img_user_icon)
                    }
                }

                override fun onAlbumResult(uri: Uri?, path: String?) {
                    if (uri != null) {
                        GlideUtils.loadUri(this@UserInfoActivity, uri, img_user_icon)
                        mCameraPath = CameraUtil.getRealFilePath(this@UserInfoActivity, uri)
                    }
                    if (path != null) {
                        mCameraPath = path
                        GlideUtils.loadPath(this@UserInfoActivity, path, img_user_icon)
                    }
                }

            })
    }


    /**
     * 完成
     */
    private fun end() {
        val userId = SPUTil.getString(Constants.USER_NAME)
        val password = SPUTil.getString(Constants.USER_PASS)
        JMessageClient.login(userId, password, object : BasicCallback() {
            override fun gotResult(responseCode: Int, responseMessage: String) {
                if (responseCode == 0) {
                    val username = JMessageClient.getMyInfo().userName
                    val appKey = JMessageClient.getMyInfo().appKey
                    val user = User()
                    user.userName = username
                    user.userPass = appKey
                    user.userNickName = edt_nick_name.text.toString()
                    viewModel =
                        ViewModelProvider(this@UserInfoActivity).get(ContactViewModel::class.java)
                    viewModel.insertUser(user)
                    val nickName: String = edt_nick_name.text.toString()
                    val myUserInfo = JMessageClient.getMyInfo()
                    if (myUserInfo != null) {
                        myUserInfo.nickname = nickName
                    }
                    //注册时候更新昵称
                    JMessageClient.updateMyInfo(
                        UserInfo.Field.nickname,
                        myUserInfo,
                        object : BasicCallback() {
                            override fun gotResult(
                                status: Int,
                                desc: String
                            ) { //更新跳转标志
                                if (status == 0) {
                                    startActivity(MainActivity::class.java)
                                }
                            }
                        })
                    if (mCameraPath != null) {
                        runOnUiThread(Runnable {
                            JMessageClient.updateUserAvatar(
                                File(mCameraPath!!),
                                object : BasicCallback() {
                                    override fun gotResult(
                                        responseCode: Int,
                                        responseMessage: String
                                    ) {
                                        if (responseCode == 0) {
                                            SPUTil.putAsyncString(
                                                Constants.USER_AVATAR_PATH,
                                                mCameraPath
                                            )
                                        }
                                    }
                                })
                        })
                    }

                }
            }
        })
    }
}
