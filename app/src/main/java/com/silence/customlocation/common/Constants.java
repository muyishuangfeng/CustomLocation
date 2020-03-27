package com.silence.customlocation.common;


public class Constants {
    private static final int BASE_CODE = 0X01;
    /***获取定位成功*/
    public static final int MESSAGE_LOCATION_SU = BASE_CODE + 1;
    /***获取定位失败*/
    public static final int MESSAGE_LOCATION_FAILED = BASE_CODE + 2;
    /***拍照*/
    public static final int MSG_TAKE_PHOTO = BASE_CODE + 3;
    /***从相册选择*/
    public static final int MSG_CHOOSE_PHOTO = BASE_CODE + 4;


    /***名称*/
    public static final String USER_NAME = "USER_NAME";
    /***邮箱*/
    public static final String USER_EMAIL = "USER_EMAIL";
    /***密码*/
    public static final String USER_PASS = "USER_PASS";
    /***昵称*/
    public static final String USER_NICE_NAME = "USER_NICE_NAME";
    /***年龄*/
    public static final String USER_AGE = "USER_AGE";
    /***头像路径*/
    public static final String USER_AVATAR_PATH = "USER_AVATAR_PATH";
    /***头像名称*/
    public static final String USER_AVATAR_NAME = "USER_AVATAR_NAME" + ".png";


}
