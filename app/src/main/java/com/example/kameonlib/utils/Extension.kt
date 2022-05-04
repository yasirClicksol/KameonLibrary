package com.example.kameonlib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kameonlib.R
import com.example.kameonlib.network.ApiClient
import com.example.kameonlib.network.ApiInterface
import com.example.kameonlib.utils.Constants.GALLERY_PICTURE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.kameonlib.utils.Constants.PICKFILE_REQUEST_CODE
import com.example.kameonlib.utils.Constants.baseUrl
import kotlinx.android.synthetic.main.dlg_alert_delete.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception


/*****************************
 * Checking internet connectivity
 * ****************************/
fun isOnline(activity: Activity): Boolean {
    val networkInfo =
        (activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

/*****************************
 * showing offline toast
 * ****************************/
fun Context.showToastMsg(){
    Toast.makeText(this, getString(R.string.offline_msg),Toast.LENGTH_SHORT).show()
}

/*****************************
 * Gallery open intent
 * ****************************/
fun openGallery(activity: Activity) {
    val intent =
        Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    activity.startActivityForResult(
        Intent.createChooser(intent, "Select Picture"),
        GALLERY_PICTURE
    )
}

/*****************************
 * files open intent
 * ****************************/
fun openFiles(activity: Activity) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "application/pdf"
    activity.startActivityForResult(intent, PICKFILE_REQUEST_CODE)

}


/*****************************
 * Getting path for gallery selected picture
 * ****************************/
@SuppressLint("NewApi")
fun getPath(uri: Uri, context: Context): String? {
    var uri = uri
    val needToCheckUri = Build.VERSION.SDK_INT >= 19
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    // Uri is different in versions after KITKAT (Android 4.4), we need to
    // deal with different Uris.
    if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
        when {
            isExternalStorageDocument(uri) -> {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
            isDownloadsDocument(uri) -> {
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            }
            isMediaDocument(uri) -> {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                when (type) {
                    "image" -> uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(
                    split[1]
                )
            }
        }
    }
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )
        try {
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                .use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val columnIndex =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        return cursor.getString(columnIndex)
                    }
                }
        } catch (e: Exception) {
            Log.e("on getPath", "Exception", e)
        }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/*****************************
 * uploading images and files
 * ****************************/

 fun Activity.callApiUploadData(body: MultipartBody.Part, cl_show_progress: ConstraintLayout) {
    if(isOnline(this)){
        val getStorageRes = ApiClient.getInstance(baseUrl).create(ApiInterface::class.java)
        // launching a new coroutine

        cl_show_progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {

            val stringCall: Call<String?>? = getStorageRes.uploadBlobDataServer(
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    "ccef4ed7-7ec7-421d-8afb-3dce0a8be39c"
                ),
                body,
                RequestBody.create(MediaType.parse("text/plain"), "yasirM"),
                RequestBody.create(MediaType.parse("text/plain"), "mobile app")
            )
            stringCall?.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    cl_show_progress.visibility = View.GONE
                    Toast.makeText(this@callApiUploadData,getString(R.string.upload),Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    cl_show_progress.visibility = View.GONE
                    Toast.makeText(this@callApiUploadData,getString(R.string.some_went_wrong),Toast.LENGTH_SHORT).show()

                }
            })
        }
    }
    else{
        showToastMsg()
    }

}

/*****************************
 * Defining types and call for api
 * ****************************/

fun Activity.callForUploadFileImages(cl_show_progress: ConstraintLayout, data: Intent? , type:String){
    if (isOnline(this)) {
        val imageUri = data?.data
        if (imageUri != null) {

            val galleryImagePath = File(getPath(imageUri, this))
            var filetype = ""
            if (type == "image/*"){
                filetype = type
            }
            else{
                filetype = "multipart/form-data"
            }
            val requestBody = RequestBody.create(MediaType.parse(filetype), galleryImagePath)
            val body = MultipartBody.Part.createFormData(
                "file",
                galleryImagePath.name,
                requestBody
            )

            callApiUploadData(body, cl_show_progress)


        }

    } else {
        showToastMsg()
    }
}
