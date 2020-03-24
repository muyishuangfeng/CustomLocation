package com.silence.customlocation.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {


    //视图是否创建
    private var isViewCreated = false
    //数据是否加载完成
    private var isLoadDataCompleted = false
    protected var mActivity: Activity? = null
    var mRootView: View? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), container, false)
            isViewCreated = true
        }
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        if (userVisibleHint) {
            lazyLoadData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            lazyLoadData()
        } else {
            isLoadDataCompleted = false
            isViewCreated = false
        }
    }



    /**
     * 获取布局
     */
    protected abstract fun getLayoutID(): Int

    /**
     * 初始化布局
     */
    protected abstract fun initView()

    /**
     * 懒加载
     */
    fun lazyLoadData() {
        isLoadDataCompleted = true
    }

    /**
     * 通过Class跳转界面
     */
    fun startActivityForResult(cls: Class<*>?, requestCode: Int) {
        startActivityForResult(cls, null, requestCode)
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    fun startActivityForResult(
        cls: Class<*>?, bundle: Bundle?,
        requestCode: Int
    ) {
        val intent = Intent()
        intent.setClass(mActivity!!, cls!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }
    /**
     * 含有Bundle通过Class跳转界面
     */
    /**
     * 通过Class跳转界面
     */
    @JvmOverloads
    fun startActivity(cls: Class<*>?, bundle: Bundle? = null) {
        val intent = Intent()
        intent.setClass(mActivity!!, cls!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 线程
     */
    fun runOnUIThread(r: Runnable?) {
        val activity = mActivity
        if (activity != null && r != null) activity.runOnUiThread(r)
    }

    // 返回唯一的Activity实例
    val proxyActivity: BaseActivity?
        get() = mActivity as BaseActivity?

    companion object {
        private const val ARG_NUMBER = "ARG_NUMBER"
    }
}