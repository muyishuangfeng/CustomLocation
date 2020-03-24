package com.silence.customlocation.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by Silence on 2017/8/30.
 */
class FragmentDetailAdapter(fm: Fragment, list: MutableList<Fragment>) : FragmentStateAdapter(fm) {

    private var mList: MutableList<Fragment>? = null

    init {
        mList = list
    }


    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        return mList!![position]
    }


}