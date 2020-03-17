package com.silence.customlocation.util.gps;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.silence.customlocation.R;
import com.silence.customlocation.common.APP;
import com.silence.customlocation.impl.OnLocationResultListener;
import com.silence.customlocation.model.GpsModel;
import com.silence.customlocation.util.AppUtil;

public class GpsUtil {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    private boolean isCreateChannel = false;
    private OnLocationResultListener mListener;

    public GpsUtil(Context mContext, OnLocationResultListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        initLocation();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(mContext.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 运行
     */
    public void onResume() {
        //切入前台后关闭后台定位功能
        if (null != locationClient) {
            locationClient.disableBackgroundLocation(true);
        }
    }

    /**
     * 停止
     */
    public void onStop() {
        boolean isBackground = APP.Companion.isBackground();
        //如果app已经切入到后台，启动后台定位功能
        if (isBackground) {
            if (null != locationClient) {
                locationClient.enableBackgroundLocation(2001, buildNotification());
            }
        }
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (null == locationClient) {
            locationClient = new AMapLocationClient(mContext);
        }
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        //启动后台定位
        locationClient.enableBackgroundLocation(2001, buildNotification());
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (null == locationClient) {
            locationClient = new AMapLocationClient(mContext);
        }
        //关闭后台定位
        locationClient.disableBackgroundLocation(true);
        // 停止定位
        locationClient.stopLocation();
    }


    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    //解析定位结果
                    GpsModel getGpsBean = new GpsModel();
                    getGpsBean.setAddress(location.getAddress());
                    getGpsBean.setCityName(location.getCity());
                    getGpsBean.setLatitude(location.getLatitude());
                    getGpsBean.setLongitude(location.getLongitude());
                    getGpsBean.setCityCode(location.getCityCode());
                    getGpsBean.setProvince(location.getProvince());
                    getGpsBean.setCountry(location.getCountry());
                    mListener.onSuccess(getGpsBean);
                } else {
                    mListener.onFailed(location.getErrorCode());
                }
            }
        }
    };


    /**
     * 销毁定位
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /*
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 通知
     */
    @SuppressLint("NewApi")
    private Notification buildNotification() {
        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = mContext.getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(mContext.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(mContext.getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(AppUtil.getAppName(mContext))
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }


}
