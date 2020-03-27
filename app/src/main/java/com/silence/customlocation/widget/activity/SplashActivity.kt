package com.silence.customlocation.widget.activity

import android.Manifest
import android.animation.Animator
import cn.jpush.im.android.api.JMessageClient
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseActivity
import com.yk.silent.permission.HiPermission
import com.yk.silent.permission.impl.PermissionCallback
import com.yk.silent.permission.model.PermissionItem
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : BaseActivity() {


    override fun getLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun initData() {
        initPermission()
    }




    /**
     * 获取权限
     */
    private fun initPermission() {
        val permissionItems = ArrayList<PermissionItem>()
        permissionItems.add(
            PermissionItem(
                Manifest.permission.READ_PHONE_STATE,
                resources.getString(R.string.text_read), R.drawable.permission_ic_storage
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.CAMERA,
                resources.getString(R.string.text_read), R.drawable.permission_ic_storage
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.ACCESS_FINE_LOCATION,
                resources.getString(R.string.text_read), R.drawable.permission_ic_storage
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                resources.getString(R.string.text_read), R.drawable.permission_ic_storage
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.READ_CONTACTS,
                resources.getString(R.string.text_read_contact), R.drawable.permission_ic_contacts
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                resources.getString(R.string.text_read_contact), R.drawable.permission_ic_contacts
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                resources.getString(R.string.text_read_contact), R.drawable.permission_ic_contacts
            )
        )
        HiPermission.create(this)
            .title(resources.getString(R.string.permission_get))
            .msg(resources.getString(R.string.permission_desc))
            .permissions(permissionItems)
            .checkMutiPermission(object : PermissionCallback {
                override fun onClose() {
                }

                override fun onDeny(permission: String?, position: Int) {
                }

                override fun onFinish() {
                    initView()
                }

                override fun onGuarantee(permission: String?, position: Int) {

                }
            })
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        lav_splash.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                lav_splash.cancelAnimation()
                //检测账号是否登陆
                val myInfo = JMessageClient.getMyInfo()
                if (myInfo == null) {
                    startActivity(LoginActivity::class.java)
                    finish()
                } else {
                    startActivity(MainActivity::class.java)
                    finish()
                }


            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        lav_splash.playAnimation()

    }

    override fun onDestroy() {
        super.onDestroy()
        lav_splash.cancelAnimation()
    }
}
