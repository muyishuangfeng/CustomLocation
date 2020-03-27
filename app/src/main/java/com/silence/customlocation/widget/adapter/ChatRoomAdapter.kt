package com.silence.customlocation.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import cn.jpush.im.android.api.model.ChatRoomInfo
import com.silence.customlocation.R
import com.silence.customlocation.widget.adapter.viewholder.BaseAdapter
import kotlinx.android.synthetic.main.item_chat_layout.view.*

class ChatRoomAdapter(context: Context, list: MutableList<ChatRoomInfo>) :
    BaseAdapter<ChatRoomInfo>() {

    var mList: MutableList<ChatRoomInfo>? = ArrayList()
    var mContext: Context? = null
    var mInflater: LayoutInflater? = null

    init {
        mContext = context
        mList = list
        mInflater = LayoutInflater.from(mContext)
    }


    override fun getLayoutID(): Int {
        return R.layout.item_chat_layout
    }

    override fun getEmptyLayout(): Int {
        return R.layout.item_empty_layout
    }

    override fun bindData(holder: BaseViewHolder?, t: ChatRoomInfo, position: Int) {
        holder?.itemView?.txt_chat_content?.text = mList!![position].description
        holder?.itemView?.txt_chat_name?.text = mList!![position].name
    }

    override fun noData(holder: BaseViewHolder?, position: Int) {
    }
}