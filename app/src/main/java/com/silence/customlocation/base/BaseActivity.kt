package com.silence.customlocation.base

import android.os.Bundle
import android.view.Window
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.silence.customlocation.R
import com.silence.customlocation.common.ActivityStackManager
import com.silence.customlocation.widget.activity.MainActivity


abstract class BaseActivity : SwipeBackActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutID())
        ActivityStackManager.instance.addActivity(this)
        setSwipeLayout()
        initData()
    }

    /**
     * 获取布局
     */
    abstract fun getLayoutID(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()




    /**
     * 页面跳转
     *
     * @param clz
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(applicationContext, clz))
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 设置侧滑返回
     */
    private fun setSwipeLayout() {
        if (this is MainActivity) {
            setSwipeBackEnable(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragmentManager = supportFragmentManager
        for (index in 0 until fragmentManager.fragments.size) {
            //找到第一层Fragment
            val fragment = fragmentManager.fragments[index]
            if (fragment != null)
                handleResult(fragment, requestCode, resultCode, data)
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     */
    private fun handleResult(fragment: Fragment, requestCode: Int, resultCode: Int, data: Intent?) {
        //调用每个Fragment的onActivityResult
        fragment.onActivityResult(requestCode, resultCode, data)
        //找到第二层Fragment
        val childFragment = fragment.childFragmentManager.fragments
        for (f in childFragment) {
            if (f != null) {
                handleResult(f, requestCode, resultCode, data)
            }
        }

    }

    /**
     * Fragment优化
     *
     * @param targetFragment
     * @return
     */
    protected fun switchFragment(targetFragment: Fragment): FragmentTransaction {
        val currentFragment = Fragment()
        val transaction = supportFragmentManager
                .beginTransaction()
        if (!targetFragment.isAdded) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            transaction.hide(currentFragment)
            transaction.add(R.id.fl_container, targetFragment, targetFragment.javaClass.name)
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit()
        }
        currentFragment == targetFragment
        return transaction
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.instance.removeActivity(this)
    }
}