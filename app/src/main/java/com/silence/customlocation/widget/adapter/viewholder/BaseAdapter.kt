package com.silence.customlocation.widget.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silence.customlocation.impl.OnItemClickListener
import com.silence.customlocation.impl.OnItemLongClickListener


abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private var mListener: OnItemClickListener? = null
    private var mLongListener: OnItemLongClickListener? = null
    private var mList: MutableList<T>? = ArrayList()
    private val ERROR_CODE = 7011

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (getItemViewType(viewType) == ERROR_CODE) {
            return BaseViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    getEmptyLayout(),
                    parent,
                    false
                )
            )
        } else {
            return BaseViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    getLayoutID(),
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return if (mList != null) {
            mList!!.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == ERROR_CODE) {
            noData(holder, position)
        } else {
            bindData(holder, mList!![position], position)
        }
        if (mListener != null) {
            holder.itemView.setOnClickListener {
                mListener!!.itemClick(position, holder.itemView)
            }
        }
        if (mLongListener != null) {
            holder.itemView.setOnLongClickListener {
                mLongListener!!.itemLongClick(position, holder.itemView)
                true
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (mList == null || mList!!.isEmpty()) {
            return ERROR_CODE
        }
        return super.getItemViewType(position)
    }

    /**
     * ViewHolder
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 获取布局
     */
    abstract fun getLayoutID(): Int

    /**
     * 空布局
     */
    abstract fun getEmptyLayout(): Int

    /**
     * 绑定数据
     */
    abstract fun bindData(holder: BaseViewHolder?, t: T, position: Int)

    /**
     * 空数据
     */
    abstract fun noData(holder: BaseViewHolder?, position: Int)

    /**
     * 设置点击事件
     */
    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    /**
     * 设置长按事件
     */
    fun setOnLongClickListener(listener: OnItemLongClickListener) {
        mLongListener = listener
    }

}