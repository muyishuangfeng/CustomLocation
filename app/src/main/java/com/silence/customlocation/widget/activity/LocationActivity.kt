package com.silence.customlocation.widget.activity

import com.amap.api.location.AMapLocationQualityReport
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseActivity
import com.silence.customlocation.impl.OnLocationResultListener
import com.silence.customlocation.model.GpsModel
import com.silence.customlocation.util.gps.GpsUtil
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : BaseActivity(), OnLocationResultListener {

    var mGpsUtils: GpsUtil? = null

    override fun getLayoutID(): Int {
        return R.layout.activity_location
    }

    override fun initData() {
        mGpsUtils = GpsUtil(this, this)

        btn_start.setOnClickListener {
            mGpsUtils!!.startLocation()
        }
        btn_stop.setOnClickListener {
            mGpsUtils!!.stopLocation()
        }

    }


    override fun onFailed(code: Int) {
        when (code) {
            AMapLocationQualityReport.GPS_STATUS_OK ->
                txt_code.text = "GPS状态正常"
            AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER ->
                txt_code.text = "手机中没有GPS Provider，无法进行GPS定位"
            AMapLocationQualityReport.GPS_STATUS_OFF ->
                txt_code.text = "GPS关闭，建议开启GPS，提高定位质量"
            AMapLocationQualityReport.GPS_STATUS_MODE_SAVING ->
                txt_code.text =
                "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量"
            AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION ->
                txt_code.text = "没有GPS定位权限，建议开启gps定位权限"
        }
    }

    override fun onSuccess(model: GpsModel) {
        txt_result.text = model.toString()

    }


    override fun onResume() {
        super.onResume()
        mGpsUtils!!.onResume()
    }

    override fun onStop() {
        super.onStop()
        mGpsUtils!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mGpsUtils!!.destroyLocation()
    }
}
