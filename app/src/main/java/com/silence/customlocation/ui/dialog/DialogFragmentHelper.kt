package com.silence.customlocation.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseDialogFragment
import com.silence.customlocation.impl.IDialogResultListener
import com.silence.customlocation.impl.OnCallDialog
import com.silence.customlocation.impl.OnDialogCancelListener


/**
 * DialogFragment帮助类
 */
object DialogFragmentHelper {

    /**
     * 加载中的弹出窗
     */
    private val mTheme: Int = R.style.Base_AlertDialog
    private val TAG = DialogFragmentHelper::class.java.simpleName
    private val mProgressTag: String = "$TAG:progress"

    /**
     * 进度条对话框
     */
    fun showProgress(
        fragmentManager: FragmentManager?,
        message: String?
    ): BaseDialogFragment? {
        return showProgress(fragmentManager, message, true, null)
    }

    /**
     * 进度条对话框
     */
    fun showProgress(
        fragmentManager: FragmentManager?,
        message: String?,
        cancelable: Boolean
    ): BaseDialogFragment? {
        return showProgress(fragmentManager, message, cancelable, null)
    }

    /**
     * 进度条对话框
     */
    fun showProgress(
        fragmentManager: FragmentManager?, message: String?, cancelable: Boolean
        , cancelListener: OnDialogCancelListener?
    ): BaseDialogFragment? {
        val dialogFragment: BaseDialogFragment? =
            BaseDialogFragment.newInstance(object : OnCallDialog {
                override fun getDialog(context: Context?): Dialog {
                    val progressDialog = ProgressDialog(context, mTheme)
                    progressDialog.setMessage(message)
                    return progressDialog
                }
            }, cancelable, cancelListener)
        dialogFragment?.show(fragmentManager!!, mProgressTag)
        return dialogFragment
    }

    /**
     * 提示对话框
     */
    fun showTips(
        fragmentManager: FragmentManager?,
        message: String?
    ) {
        showTips(fragmentManager, message, true, null)
    }

    /**
     * 提示对话框
     */
    fun showTips(
        fragmentManager: FragmentManager?,
        message: String?,
        cancelable: Boolean
    ) {
        showTips(fragmentManager, message, cancelable, null)
    }

    /**
     * 提示对话框
     */
    fun showTips(
        fragmentManager: FragmentManager?,
        message: String?,
        cancelable: Boolean
        ,
        cancelListener: OnDialogCancelListener?
    ) {
        val dialogFragment: BaseDialogFragment? =
            BaseDialogFragment.newInstance(object : OnCallDialog {
                override fun getDialog(context: Context?): Dialog {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context!!, mTheme)
                    builder.setMessage(message)
                    return builder.create()
                }
            }, cancelable, cancelListener)
        dialogFragment?.show(fragmentManager!!, mProgressTag)
    }

    /**
     * 显示底部对话框
     */
    fun showBottomDialog(
        fragmentManager: FragmentManager?,
        resultListener: IDialogResultListener<Int>,
        cancelable: Boolean
    ): DialogFragment? {
        val dialogFragment: BaseDialogFragment? =
            BaseDialogFragment.newInstance(object : OnCallDialog {
                @SuppressLint("InflateParams")
                override fun getDialog(context: Context?): Dialog {
                    val mDialog =
                        Dialog(context!!, mTheme)
                    val mInflater = context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val layout = mInflater.inflate(
                        R.layout.dialog_camera_layout, null
                    ) as LinearLayout
                    val mFullFillWidth = 10000
                    // 设置宽度
                    layout.minimumWidth = mFullFillWidth
                    val mTxtTakePhoto =
                        layout.findViewById<TextView>(R.id.txt_dialog_camera)
                    val mTxtChoose = layout.findViewById<TextView>(R.id.txt_dialog_photo)
                    val mTxtCancel = layout.findViewById<TextView>(R.id.txt_dialog_cancel)
                    //拍照
                    mTxtTakePhoto.setOnClickListener {
                        resultListener.onDataResult(0)
                        mDialog.dismiss()
                    }
                    //从相册选择
                    mTxtChoose.setOnClickListener {
                        resultListener.onDataResult(1)
                        mDialog.dismiss()
                    }
                    //取消
                    mTxtCancel.setOnClickListener {
                        mDialog.dismiss()
                    }
                    // 获取窗口
                    val window: Window? = mDialog.window
                    window?.setBackgroundDrawableResource(R.color.colorTransparent)
                    // 改变对话框透明度
                    val mParams: WindowManager.LayoutParams = window!!.getAttributes()
                    mParams.x = 0
                    val mMakeButton = -1000
                    mParams.y = mMakeButton
                    mParams.gravity = Gravity.BOTTOM
                    mDialog.onWindowAttributesChanged(mParams)
                    // 设置点击外面取消对话框
                    mDialog.setCanceledOnTouchOutside(false)
                    // 绑定布局
                    mDialog.setContentView(layout)
                    mDialog.show()
                    return mDialog
                }


            }, cancelable, null)
        dialogFragment?.show(fragmentManager!!, mProgressTag)
        return null
    }


}