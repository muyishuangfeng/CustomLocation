package com.silence.customlocation.widget.activity

import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseActivity
import com.silence.customlocation.common.Constants
import com.silence.customlocation.db.ContactViewModel
import com.silence.customlocation.db.User
import com.silence.customlocation.util.SPUTil
import com.silence.customlocation.util.RegularUtil.isContainChinese
import com.silence.customlocation.util.RegularUtil.whatContain
import com.silence.customlocation.util.RegularUtil.whatStartWith
import com.silence.customlocation.util.ToastUtil
import com.yk.silence.toolbar.CustomTitleBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), CustomTitleBar.TitleClickListener {

    private var mIsLogin = false
    private lateinit var viewModel: ContactViewModel


    override fun getLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        SPUTil.init(this)
        title_login.setRightTitle(resources.getString(R.string.text_login))
        title_login.setTitleClickListener(this)
        btn_register.setOnClickListener {
            login()
        }
    }


    override fun onLeftClick() {
    }

    override fun onRightClick() {
        title_login.setRightTitle(resources.getString(R.string.text_register))
        mIsLogin = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsLogin = false
    }

    /**
     * 登录
     */
    private fun login() {
        //登陆验证
        if (TextUtils.isEmpty(edt_account.text.toString())) {
            ToastUtil.getInstance().shortToast(this, "用户名不能为空")
            return
        }
        if (TextUtils.isEmpty(edt_pass.text.toString())) {
            ToastUtil.getInstance().shortToast(this, "密码不能为空")
            edt_pass.setShakeAnimation()
            return
        }
        if (edt_account.text!!.length < 4 || edt_account.text!!.length > 128) {
            edt_account.setShakeAnimation()
            ToastUtil.getInstance().shortToast(this, "用户名为4-128位字符")
            return
        }
        if (edt_pass.text!!.length < 4 || edt_pass.text!!.length > 128) {
            edt_pass.setShakeAnimation()
            ToastUtil.getInstance().shortToast(this, "密码为4-128位字符")
            return
        }
        if (isContainChinese(edt_account.text.toString())) {
            edt_account.setShakeAnimation()
            ToastUtil.getInstance().shortToast(this, "用户名不支持中文")
            return
        }
        if (!whatStartWith(edt_account.text.toString())) {
            edt_account.setShakeAnimation()
            ToastUtil.getInstance().shortToast(this, "用户名以字母或者数字开头")
            return
        }
        if (!whatContain(edt_account.text.toString())) {
            edt_account.setShakeAnimation()
            ToastUtil.getInstance().shortToast(this, "只能含有: 数字 字母 下划线 . - @")
            return
        }
        //登陆
        if (!mIsLogin) {
            JMessageClient.login(
                edt_account.text.toString(),
                edt_pass.text.toString(),
                object : BasicCallback() {
                    override fun gotResult(responseCode: Int, responseMessage: String) {
                        if (responseCode == 0) {
                            SPUTil.putAsyncString(Constants.USER_NAME, edt_account.text.toString())
                            SPUTil.putAsyncString(Constants.USER_PASS, edt_pass.text.toString())
                            val myInfo = JMessageClient.getMyInfo()
                            val avatarFile = myInfo.avatarFile
                            //登陆成功,如果用户有头像就把头像存起来,没有就设置null
                            if (avatarFile != null) {
                                SPUTil.putAsyncString(
                                    Constants.USER_AVATAR_PATH,
                                    avatarFile.absolutePath
                                )
                            }
                            //数据库保存
                            val username = myInfo.userName
                            val appKey = myInfo.appKey
                            val user = User()
                            user.userName = username
                            user.appKey = appKey
                            viewModel =
                                ViewModelProvider(this@LoginActivity).get(ContactViewModel::class.java)
                            viewModel.insertUser(user)
                            SPUTil.putAsyncString(Constants.USER_NAME, edt_account.text.toString())
                            startActivity(MainActivity::class.java)
                            ToastUtil.getInstance().shortToast(this@LoginActivity, "登陆成功")
                            finish()
                        } else {
                            ToastUtil.getInstance()
                                .shortToast(this@LoginActivity, "登陆失败$responseMessage")
                        }
                    }
                })
            //注册
        } else {
            JMessageClient.register(
                edt_account.text.toString(),
                edt_pass.text.toString(),
                object : BasicCallback() {
                    override fun gotResult(i: Int, s: String) {
                        if (i == 0) {
                            SPUTil.putAsyncString(Constants.USER_NAME, edt_account.text.toString())
                            SPUTil.putAsyncString(Constants.USER_PASS, edt_pass.text.toString())
                            startActivity(UserInfoActivity::class.java)
                            ToastUtil.getInstance().shortToast(this@LoginActivity, "注册成功")
                        }
                    }
                })
        }
    }
}