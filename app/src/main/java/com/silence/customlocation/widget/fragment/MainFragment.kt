package com.silence.customlocation.widget.fragment

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseFragment
import com.silence.customlocation.common.Constants
import com.silence.customlocation.model.GpsModel
import com.silence.customlocation.util.gps.GetGpsThread
import com.silence.customlocation.widget.adapter.FragmentDetailAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    var mGpsThread: GetGpsThread? = null
    var mGpsModel: GpsModel? = null
    var mList: MutableList<Fragment>? = ArrayList()


    override fun getLayoutID(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        mList?.add(HomeFragment())
        mList?.add(ContactFragment())
        mList?.add(MyselfFragment())
        main_view_pager?.adapter = FragmentDetailAdapter(this, mList!!)
        bottom_btn_view?.setOnNavigationItemSelectedListener(this)
        initEvent()

        mGpsThread = GetGpsThread(mActivity, mHandler)
        mGpsThread?.start()
    }

    override fun onStop() {
        super.onStop()
        if (mGpsThread != null) {
            mGpsThread!!.stopLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mGpsThread != null) {
            mGpsThread!!.destroyLocation()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                main_view_pager.setCurrentItem(0, true)
            }
            R.id.location -> {
                main_view_pager.setCurrentItem(1, true)
            }
            R.id.mine -> {
                main_view_pager.setCurrentItem(2, true)
            }
        }
        return true
    }

    /**
     * Handler
     */
    private var mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            when (msg.what) {
                Constants.MESSAGE_LOCATION_FAILED -> {//定位失败

                }
                Constants.MESSAGE_LOCATION_SU -> {//定位成功
                    mGpsModel = msg.obj as GpsModel?
                    Log.e("TAG", mGpsModel.toString())
                    Toast.makeText(
                        mActivity, mGpsModel?.address,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    /**
     * 设置viewPager2事件
     */
    private fun initEvent() {
        main_view_pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        bottom_btn_view?.menu?.getItem(position)?.setIcon(R.mipmap.ic_launcher)
                        bottom_btn_view?.menu?.getItem(position)?.setTitle(R.string.text_home)
                        bottom_btn_view?.menu?.getItem(position)?.isChecked = true
                    }
                    1 -> {
                        bottom_btn_view?.menu?.getItem(position)?.setIcon(R.mipmap.ic_launcher)
                        bottom_btn_view?.menu?.getItem(position)?.setTitle(R.string.text_location)
                        bottom_btn_view?.menu?.getItem(position)?.isChecked = true
                    }
                    2 -> {
                        bottom_btn_view?.menu?.getItem(position)?.setIcon(R.mipmap.ic_launcher)
                        bottom_btn_view?.menu?.getItem(position)?.setTitle(R.string.text_mine)
                        bottom_btn_view?.menu?.getItem(position)?.isChecked = true
                    }
                }
            }
        })
    }
}