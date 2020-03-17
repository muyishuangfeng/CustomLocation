package com.silence.customlocation.widget.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silence.customlocation.R
import com.silence.customlocation.common.Constants
import com.silence.customlocation.impl.OnItemInviteListener
import com.silence.customlocation.model.ContactBean
import com.silence.customlocation.model.GpsModel
import com.silence.customlocation.util.gps.GetGpsThread
import com.silence.customlocation.widget.adapter.ContactListAdapter
import com.silence.customlocation.base.BaseActivity
import com.silence.customlocation.util.contact.ContactUtil
import com.yk.silence.toolbar.CustomTitleBar
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BaseActivity(), OnItemInviteListener,
    SwipeRefreshLayout.OnRefreshListener, CustomTitleBar.TitleClickListener {

    private var mList: MutableList<ContactBean>? = ArrayList()
    private var mAdapter: ContactListAdapter? = null
    var mGpsThread: GetGpsThread? = null
    var mGpsModel: GpsModel? = null

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        srl_contact_list.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        srl_contact_list.isRefreshing = true
        srl_contact_list.setOnRefreshListener(this)
        mGpsThread = GetGpsThread(this, mHandler)
        mGpsThread?.start()
        title_contact.setTitleClickListener(this)
        loadData()
    }

    override fun onInviteClick(position: Int) {
        val mBean = mList?.get(position)
        Toast.makeText(this, mBean.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onRefresh() {
        srl_contact_list.isRefreshing = mList!!.isEmpty()

    }

    override fun onLeftClick() {
    }

    override fun onRightClick() {
        startActivity(Intent(this, LocationActivity::class.java))
    }


    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        mAdapter = ContactListAdapter(this, mList!!)
        rlv_contact.layoutManager = LinearLayoutManager(this)
        rlv_contact.adapter = mAdapter
        mAdapter!!.mListener = this
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        Observable.create<MutableList<ContactBean>> { subscriber ->
            mList = ContactUtil.searchAllContacts(this)
            srl_contact_list.isRefreshing = true
            subscriber.onNext(mList!!)
            subscriber.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MutableList<ContactBean>> {
                override fun onComplete() {
                    srl_contact_list.isRefreshing = false

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: MutableList<ContactBean>) {
                    initAdapter()
                }

                override fun onError(e: Throwable) {
                }

            })
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
                        this@MainActivity, mGpsModel?.address,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
