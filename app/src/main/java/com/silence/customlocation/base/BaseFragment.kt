package com.silence.customlocation.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

@Suppress("DEPRECATION")
abstract class BaseFragment : Fragment() {

    companion object {
        private const val ARG_NUMBER = "ARG_NUMBER"
    }

    // 返回唯一的Activity实例
    val proxyActivity: BaseActivity?
        get() = mActivity as BaseActivity?
    protected var mActivity: Activity? = null
    var mRootView: View? = null
    //是否懒加载
    var isLazyLoad = true
    //是否加载数据（暂时用于第一次加载判断，以后也许会有其他情况）
    private var isNeedLoad = true


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), container, false)
            //isViewCreated = true
        }
        return mRootView
    }


    override fun onResume() {
        super.onResume()
        //如果是第一次且是懒加载
        //执行初始化方法
        if (isNeedLoad && isLazyLoad) {
            lazyLoadData()
            //数据已加载，置false，避免每次切换都重新加载数据
            isNeedLoad = false
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
     * 返回
     */
    open fun onBack() {}


    /**
     * 懒加载
     */
    open fun lazyLoadData() {
        //isLoadDataCompleted = true
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


}