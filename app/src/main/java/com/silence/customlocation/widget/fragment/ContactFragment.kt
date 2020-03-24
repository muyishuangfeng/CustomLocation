package com.silence.customlocation.widget.fragment

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseFragment
import com.silence.customlocation.db.Contact
import com.silence.customlocation.db.ContactViewModel
import com.silence.customlocation.impl.OnItemInviteListener
import com.silence.customlocation.model.ContactBean
import com.silence.customlocation.util.contact.ContactUtil
import com.silence.customlocation.widget.adapter.ContactListAdapter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contact.*
import java.util.ArrayList

class ContactFragment : BaseFragment(), OnItemInviteListener,
    SwipeRefreshLayout.OnRefreshListener {

    private var mList: MutableList<Contact>? = ArrayList()
    private var mAdapter: ContactListAdapter? = null
    private lateinit var viewModel: ContactViewModel

    override fun getLayoutID(): Int {
        return R.layout.fragment_contact
    }

    override fun initView() {
        srl_contact_list.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        srl_contact_list.isRefreshing = true
        srl_contact_list.setOnRefreshListener(this)
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        viewModel.allContacts.observe(this, androidx.lifecycle.Observer {
            mList = it
            srl_contact_list.isRefreshing = mList!!.isEmpty()
            initAdapter(mList!!)
        })
    }


    override fun onRefresh() {

    }

    override fun onInviteClick(position: Int) {
        val mBean = mList?.get(position)
        Toast.makeText(mActivity, mBean.toString(), Toast.LENGTH_LONG).show()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter(mList: MutableList<Contact>) {
        mAdapter = ContactListAdapter(mActivity!!, mList)
        rlv_contact.layoutManager = LinearLayoutManager(mActivity!!)
        rlv_contact.adapter = mAdapter
        mAdapter!!.mListener = this
        mAdapter!!.notifyDataSetChanged()
    }


}