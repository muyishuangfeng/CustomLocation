package com.silence.customlocation.widget.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ChatRoomManager
import cn.jpush.im.android.api.callback.RequestCallback
import cn.jpush.im.android.api.model.ChatRoomInfo
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseFragment
import com.silence.customlocation.impl.OnItemClickListener
import com.silence.customlocation.widget.activity.ChatActivity
import com.silence.customlocation.widget.adapter.ChatRoomAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

    private val mPageCount = 15
    private var mList: MutableList<ChatRoomInfo>? = ArrayList()


    override fun getLayoutID(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        initAdapter()

    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        ChatRoomManager.getChatRoomListByApp(
            0, mPageCount,
            object :
                RequestCallback<List<ChatRoomInfo>>() {
                override fun gotResult(
                    i: Int,
                    s: String,
                    result: List<ChatRoomInfo>
                ) {
                    if (i == 0) {
                        mList!!.clear()
                        mList!!.addAll(result)
                        if (rlv_chat.adapter != null) {
                            rlv_chat.adapter!!.notifyDataSetChanged()
                        }
                    }
                    refreshLayout.finishRefresh()
                }
            })
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        ChatRoomManager.getChatRoomListByApp(
            mList!!.size, mPageCount,
            object :
                RequestCallback<List<ChatRoomInfo>>() {
                override fun gotResult(
                    i: Int,
                    s: String,
                    result: List<ChatRoomInfo>
                ) {
                    if (i == 0) {
                        mList!!.addAll(result)
                        if (rlv_chat.adapter != null) {
                            rlv_chat.adapter!!.notifyDataSetChanged()
                        }
                    }
                    refreshLayout.finishLoadMore()
                }
            })
    }

    override fun itemClick(position: Int, itemView: View) {
        val mInfo = mList!![position]
        val intent = Intent(mActivity, ChatActivity::class.java)
        intent.putExtra("chatRoomId", mInfo.roomID)
        intent.putExtra("chatRoomName", mInfo.name)
        startActivity(intent)
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        ChatRoomManager.getChatRoomListByApp(
            0, mPageCount,
            object :
                RequestCallback<List<ChatRoomInfo>>() {
                override fun gotResult(
                    i: Int, s: String,
                    result: List<ChatRoomInfo>
                ) {
                    if (i == 0) {
                        mList?.addAll(result)
                    }
                    rlv_chat.adapter = ChatRoomAdapter(mActivity!!, mList!!)
                    rlv_chat.layoutManager = LinearLayoutManager(mActivity)
                    (rlv_chat.adapter as ChatRoomAdapter).setOnClickListener(this@HomeFragment)
                }
            })
    }


}