package com.silence.customlocation.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDex


class APP : Application() {


    companion object {
        private var count = 0
        /**
         * 判断app是否在后台
         */
        fun isBackground(): Boolean {
            return count <= 0
        }
    }


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this);
        registerLifecycle()
    }

    private fun registerLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity?, savedInstanceState: Bundle?
            ) {
            }

            override fun onActivityStarted(activity: Activity?) {
                count++
            }

            override fun onActivityResumed(activity: Activity?) {}
            override fun onActivityPaused(activity: Activity?) {}
            override fun onActivityStopped(activity: Activity?) {
                if (count > 0) {
                    count--
                }
            }

            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle
            ) {
            }

            override fun onActivityDestroyed(activity: Activity?) {}
        })
    }


}
