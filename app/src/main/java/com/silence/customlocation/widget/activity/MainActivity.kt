package com.silence.customlocation.widget.activity

import androidx.navigation.Navigation
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseActivity

class MainActivity : BaseActivity() {


    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun initData() {

    }


    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.main_navigation).navigateUp()


}
