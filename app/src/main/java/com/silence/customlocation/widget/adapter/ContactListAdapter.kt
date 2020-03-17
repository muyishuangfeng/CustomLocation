package com.silence.customlocation.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silence.customlocation.R
import com.silence.customlocation.impl.OnItemInviteListener
import com.silence.customlocation.model.ContactBean
import kotlinx.android.synthetic.main.item_contact_list_layout.view.*

class ContactListAdapter(context: Context, list: MutableList<ContactBean>) :
        RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {

    private var mContext = context
    private var mList: MutableList<ContactBean>? = ArrayList()
    private var mInflater: LayoutInflater? = null
    var mListener: OnItemInviteListener? = null

    init {
        this.mInflater = LayoutInflater.from(mContext)
        this.mList=list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return ContactListViewHolder(mInflater!!.inflate(
            R.layout.item_contact_list_layout, parent,
                false))
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.itemView.txt_item_contact_name.text = mList?.get(position)?.mName
        holder.itemView.txt_item_contact_phone.text = mList?.get(position)?.mPhone
        if (mListener != null) {
            holder.itemView.setOnClickListener {
                mListener!!.onInviteClick(position)
            }
        }
    }


    class ContactListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
}