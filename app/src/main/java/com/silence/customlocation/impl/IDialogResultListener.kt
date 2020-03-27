package com.silence.customlocation.impl

/**
 * DialogFragment结果接口
 */
interface IDialogResultListener<T> {
    fun onDataResult(result: T)
}