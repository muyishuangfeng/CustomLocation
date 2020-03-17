package com.silence.customlocation.common

import android.app.Activity

import java.lang.ref.WeakReference
import java.util.Stack

class ActivityStackManager private constructor() {
    /**
     * 获取activity栈
     *
     * @return
     */
   private var activityStack: Stack<Activity>? = null
        private set
    //弱引用
    private var mCurrentActivityWeakRef: WeakReference<Activity>? = null

    /**
     * 获取实时activity
     *
     * @return
     */
    /**
     * 设置实时activity
     *
     * @param activity
     */
    var currentActivity: Activity?
        get() {
            var currentActivity: Activity? = null
            if (mCurrentActivityWeakRef == null) {
                currentActivity = mCurrentActivityWeakRef!!.get()
            }
            return currentActivity
        }
        set(activity) {
            mCurrentActivityWeakRef = WeakReference<Activity>(activity)
        }


    /**
     * 设置栈顶activity
     *
     * @param activity
     */
    var topActivity: Activity?
        get() = if (activityStack != null && activityStack!!.size > 0) {
            activityStack!!.peek()
        } else null
        set(activity) {
            if (activityStack != null && activityStack!!.size > 0) {
                if (activityStack!!.search(activity) == -1) {
                    activityStack!!.push(activity)
                    return
                }

                val location = activityStack!!.search(activity)
                if (location != 1) {
                    activityStack!!.remove(activity)
                    activityStack!!.push(activity)
                }
            }
        }

    private object InstanceHolder {
        val sInstance = ActivityStackManager()
    }

    /**
     * 添加
     *
     * @param activity
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }

        if (activityStack!!.search(activity) == -1) {
            activityStack!!.push(activity)
        }
    }

    /**
     * 移除
     *
     * @param activity
     */
    fun removeActivity(activity: Activity) {
        if (activityStack != null && activityStack!!.size > 0) {
            activityStack!!.remove(activity)
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 是否在栈顶
     *
     * @param activity
     * @return
     */
    fun isTopActivity(activity: Activity): Boolean {
        return activity == activityStack!!.peek()
    }

    /**
     * 结束栈顶activity
     */
    fun finishTopActivity() {
        if (activityStack != null && activityStack!!.size > 0) {
            val activity = activityStack!!.pop()
            activity?.finish()
        }
    }

    /**
     * 关闭所有activity
     */
    fun finishAllActivity() {
        if (activityStack != null && activityStack!!.size > 0) {
            while (!activityStack!!.empty()) {
                val activity = activityStack!!.pop()
                activity?.finish()
            }

            activityStack!!.clear()
            activityStack = null
        }
    }

    companion object {

        /**
         * 单例模式
         *
         * @return
         */
        val instance: ActivityStackManager
            get() = InstanceHolder.sInstance
    }
}
