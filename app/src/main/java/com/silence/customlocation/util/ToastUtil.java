package com.silence.customlocation.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;



public class ToastUtil {

    private static volatile ToastUtil sInstance;
    private Toast mToast;

    private ToastUtil() {
    }

    /**
     * 单例模式
     */
    public static ToastUtil getInstance() {
        if (sInstance == null) {
            synchronized (ToastUtil.class) {
                if (sInstance == null) {
                    sInstance = new ToastUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 长时间显示
     */
    public final void longToast(Context context, int id) {
        longToast(context.getApplicationContext(), context.getApplicationContext().getString(id));
    }

    /**
     * 长时间显示
     */
    public final void longToast(Context context, final String toast) {
        toast(context.getApplicationContext(), toast, Toast.LENGTH_LONG);
    }

    /**
     * 弹出
     */
    private void toast(final Context context, final String toast,
                       final int length) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            doShowToast(context.getApplicationContext(), toast, length);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    doShowToast(context.getApplicationContext(), toast, length);
                }
            });
        }
    }

    /**
     * 显示
     */
    private void doShowToast(Context context, String toast, int length) {
        try {
            final Toast t = getToast(context.getApplicationContext());
            t.setText(toast);
            t.setDuration(length);
            t.show();
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), toast, length).show();
        }
    }

    /**
     * 获取
     */
    private Toast getToast(Context context) {
        if (mToast != null) {
            mToast = new Toast(context.getApplicationContext());
        }
        return mToast;
    }

    /**
     * 短时间显示
     */
    public final void shortToast(Context context, int id) {
        shortToast(context.getApplicationContext(), context.getApplicationContext().getString(id));
    }

    /**
     * 短时间显示
     */
    public final void shortToast(Context context, String toast) {
        toast(context.getApplicationContext(), toast, Toast.LENGTH_SHORT);
    }

    public final void shortOrLongToast(Context context, int id, int length) {
        shortOrLongToast(context.getApplicationContext(), context.getApplicationContext().getString(id), length);
    }

    public final void shortOrLongToast(Context context, String res, int length) {
        toast(context.getApplicationContext(), res, length);
    }


    /**
     * 结束
     */
    public void onDestroy() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
