package com.silence.customlocation.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentLazyStateAdapter(fragment: Fragment, private val fragments: MutableList<Fragment>) :
    FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}