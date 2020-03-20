package com.silence.customlocation.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

    private static SharedPreferences settings;

    private PreferencesUtils() {
        throw new AssertionError();
    }

    public static void init(Context context) {
        settings = context.getApplicationContext()
                .getSharedPreferences("Custom_Location" +
                        AppUtil.getPackageName(context.getApplicationContext()), Context.MODE_PRIVATE);
    }


    /**
     * 写入String数据（同步操作）
     */
    public static boolean putSyncString(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 写入String数据（异步操作）
     */
    public static void putAsyncString(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取String数据（不带默认值）
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取String数据（带默认值）
     */
    public static String getString(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    /**
     * 写入Int数据（同步操作）
     */
    public static boolean putSyncInt(String key, int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 写入Int数据（异步操作）
     */
    public static void putAsyncInt(String key, int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取String数据（不带默认值）
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * 获取String数据（带默认值）
     */
    public static int getInt(String key, int defaultValue) {
        return settings.getInt(key, defaultValue);
    }

    /**
     * 写入long数据（同步操作）
     */
    public static boolean putSyncLong(String key, long value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 写入long数据（同步操作）
     */
    public static void putAsyncLong(String key, long value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 获取long数据（不带默认值）
     */
    public static long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * 获取long数据（带默认值）
     */
    public static long getLong(String key, long defaultValue) {
        return settings.getLong(key, defaultValue);
    }

    /**
     * 写入float数据（同步操作）
     */
    public static boolean putSyncFloat(String key, float value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 写入float数据（异步操作）
     */
    public static void putAsyncFloat(String key, float value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 获取float数据（不带默认值）
     */
    public static float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * 获取long数据（带默认值）
     */
    public static float getFloat(String key, float defaultValue) {
        return settings.getFloat(key, defaultValue);
    }


    /**
     * 写入boolean数据（同步操作）
     */
    public static boolean putSyncBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 写入boolean数据（异步操作）
     */
    public static void putAsyncBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取boolean数据（不带默认值）
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 获取boolean数据（不带默认值）
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 异步移除数据
     */
    public static void removeAsync(String key) {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 同步移除数据
     */
    public static boolean removeSync(String key) {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 异步清除所有
     */
    public static void clearSync() {
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().apply();
    }

    /**
     * 同步清除所有
     */
    public static boolean clearAsync() {
        SharedPreferences.Editor editor = settings.edit();
        return editor.clear().commit();
    }
}
