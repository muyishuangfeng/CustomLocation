package com.silence.customlocation.widget.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.silence.customlocation.R
import com.silence.customlocation.base.BaseActivity
import com.silence.customlocation.common.Constants
import com.silence.customlocation.util.glide.GlideUtils
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserInfoActivity : BaseActivity() {

    //是否是Android 10以上手机
    private val isAndroidQ =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    //用于保存拍照图片的uri
    private var mCameraUri: Uri? = null
    //用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private var mCameraImagePath: String? = null


    override fun getLayoutID(): Int {
        return R.layout.activity_user_info
    }

    override fun initData() {
        img_user_icon.setOnClickListener {
            openCamera()
        }
        btn_finish.setOnClickListener {

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.MSG_TAKE_PHOTO -> {//拍照
                if (resultCode == Activity.RESULT_OK) {
                    if (isAndroidQ) {
                        // Android 10 使用图片uri加载
                        GlideUtils.loadUri(this,mCameraUri,img_user_icon)
                    } else {
                        // 使用图片路径加载
                        GlideUtils.loadPath(this,mCameraImagePath,img_user_icon)
                    }
                }
            }
        }
    }


    /**
     * 打开相机
     */
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 判断是否有相机
        if (intent.resolveActivity(packageManager) != null) {
            var mPhotoFile: File? = null
            var mPhotoUri: Uri? = null
            if (isAndroidQ) {
                mPhotoUri = createImageUri()
            } else {
                try {
                    mPhotoFile = createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (mPhotoFile != null) {
                    mCameraImagePath = mPhotoFile.absolutePath
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        mPhotoUri = FileProvider.getUriForFile(
                            this, packageName
                                    + ".fileprovider", mPhotoFile
                        );
                    } else {
                        mPhotoUri = Uri.fromFile(mPhotoFile);
                    }
                }
            }
            mCameraUri = mPhotoUri
            if (mPhotoUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(intent, Constants.MSG_TAKE_PHOTO)
            }

        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private fun createImageUri(): Uri? {
        val status = Environment.getExternalStorageState()
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        return if (status == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
            )
        } else {
            contentResolver.insert(
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
    private fun createImageFile(): File? {
        val imageName: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) {
            storageDir.mkdir()
        }
        val tempFile = File(storageDir, imageName)
        return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            null
        } else tempFile
    }
}
