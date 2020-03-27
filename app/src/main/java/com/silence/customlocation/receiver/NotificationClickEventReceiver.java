package com.silence.customlocation.receiver;

import android.content.Context;

import cn.jpush.im.android.api.JMessageClient;

public class NotificationClickEventReceiver {


    public NotificationClickEventReceiver(Context context) {
        //注册接收消息事件
        JMessageClient.registerEventReceiver(context);
    }


}
