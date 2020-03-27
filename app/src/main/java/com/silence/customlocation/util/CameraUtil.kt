package com.silence.customlocation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import com.silence.customlocation.common.Constants
import com.silence.customlocation.impl.OnCameraResultListener
import java.io.File
import java.io.FileDescriptor
import java.io.IOException


object CameraUtil {

    //是否是Android 10以上手机
    private val isAndroidQ =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    //用于保存拍照图片的uri
    private var mCameraUri: Uri? = null
    //用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private var mCameraImagePath: String? = null


    /**
     * 打开相机
     */
    fun openCamera(activity: Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 判断是否有相机
        if (intent.resolveActivity(activity.packageManager) != null) {
            var mPhotoFile: File? = null
            var mPhotoUri: Uri? = null
            if (isAndroidQ) {
                mPhotoUri = createImageUri(activity)
            } else {
                try {
                    mPhotoFile = createImageFile(activity)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (mPhotoFile != null) {
                    mCameraImagePath = mPhotoFile.absolutePath
                    mPhotoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        FileProvider.getUriForFile(
                            activity, activity.packageName
                                    + ".fileprovider", mPhotoFile
                        );
                    } else {
                        Uri.fromFile(mPhotoFile);
                    }
                }
            }
            mCameraUri = mPhotoUri
            if (mPhotoUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                activity.startActivityForResult(intent, Constants.MSG_TAKE_PHOTO)
            }

        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private fun createImageUri(activity: Activity): Uri? {
        val status = Environment.getExternalStorageState()
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        return if (status == Environment.MEDIA_MOUNTED) {
            activity.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
            )
        } else {
            activity.contentResolver.insert(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI, ContentValues()
            )
        }
    }

    /**
     * 创建保存图片的文件
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createImageFile(activity: Activity): File? {
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) {
            storageDir.mkdir()
        }
        val tempFile = File(storageDir, Constants.USER_AVATAR_NAME)
        return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            null
        } else tempFile
    }

    /**
     * 从相册选择
     *
     * @param activity
     */
    fun choosePhoto(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = "android.intent.action.PICK"
        intent.addCategory("android.intent.category.DEFAULT")
        activity.startActivityForResult(intent, Constants.MSG_CHOOSE_PHOTO)
    }


    /**
     * 结果
     */
    fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        listener: OnCameraResultListener
    ) {
        when (requestCode) {
            Constants.MSG_TAKE_PHOTO -> {//拍照
                if (resultCode == Activity.RESULT_OK) {
                    if (isAndroidQ) {
                        // Android 10 使用图片uri加载
                        listener.onCameraResult(mCameraUri, null)
                    } else {
                        // 使用图片路径加载
                        listener.onCameraResult(null, mCameraImagePath)
                    }
                }
            }
            Constants.MSG_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data?.data == null) {
                        return;
                    }
                    if (isAndroidQ) {
                        // Android 10 使用图片uri加载
                        listener.onAlbumResult(null, getUri(activity))
                    } else {
                        // 使用图片路径加载
                        listener.onAlbumResult(data.data, null)
                    }

                }
            }
        }
    }


    @SuppressLint("Recycle")
    private fun getImageContentUri(context: Context, path: String): Uri? {
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(path),
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri =
                Uri.parse("content://media/external/images/media")
            Uri.withAppendedPath(baseUri, "" + id)
        } else { // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (File(path).exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, path)
                context.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                null
            }
        }
    }

    /**
     * 通过uri加载图片
     */
    private fun getBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        try {
            val parcelFileDescriptor =
                context.contentResolver.openFileDescriptor(uri!!, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    fun getRealFilePath(
        context: Context,
        uri: Uri?
    ): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver
                .query(uri, arrayOf(ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * 从相册选择（Android10）
     */
    @SuppressLint("Recycle")
    private fun getUri(activity: Activity): String? {
        val IMAGE_PROJECTION = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val imageCursor: Cursor? = activity.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[4] + " DESC"
        )
        var path =
            imageCursor?.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
        val name =
            imageCursor?.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
        val id = imageCursor?.getInt(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
        val folderPath =
            imageCursor?.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
        val folderName =
            imageCursor?.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
        //Android Q 公有目录只能通过Content Uri + id的方式访问，以前的File路径全部无效，如果是Video，记得换成MediaStore.Videos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                .buildUpon()
                .appendPath(id.toString()).build().toString()
        }
        return path
    }

}