package com.silence.customlocation.net.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

public class MemoryCache {


    private final int DEFAULT_MEM_CACHE_SIZE = 1024 * 12;
    private LruCache<String, Bitmap> mMemoryCache;
    private final String TAG = "MemoryCache";

    public MemoryCache(float sizePer) {
        init(sizePer);
    }

    /**
     * 初始化
     */
    private void init(float sizePer) {
        int cacheSize = DEFAULT_MEM_CACHE_SIZE;
        if (sizePer > 0) {
            cacheSize = Math.round(sizePer * Runtime.getRuntime().maxMemory() / 1024);
        }

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
    }

    /**
     * 获取图片大小
     *
     * @param bitmap 图片大小
     * @return 大小
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }

        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 获取图片
     *
     * @param url 图片链接
     * @return 图片
     */
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        if (mMemoryCache != null) {
            bitmap = mMemoryCache.get(url);
        }
        if (bitmap != null) {
            Log.e(TAG, "Memory cache exiet");
        }

        return bitmap;
    }

    /**
     * 添加图片到内存中
     *
     * @param url    图片链接
     * @param bitmap 图片
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (url == null || bitmap == null) {
            return;
        }

        mMemoryCache.put(url, bitmap);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }
}

