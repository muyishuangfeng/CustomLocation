package com.silence.customlocation.impl

import com.silence.customlocation.model.GpsModel

interface OnLocationResultListener {

    fun onSuccess(model: GpsModel)

    fun onFailed(code: Int)
}