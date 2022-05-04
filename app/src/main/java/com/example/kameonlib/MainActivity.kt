package com.example.kameonlib

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kameonlib.adapter.StorageAdapter
import com.example.kameonlib.model.GetStorageModel
import com.example.kameonlib.network.ApiClient
import com.example.kameonlib.network.ApiInterface
import com.example.kameonlib.utils.*
import com.example.kameonlib.utils.Constants.GALLERY_PICTURE
import com.example.kameonlib.utils.Constants.PICKFILE_REQUEST_CODE
import com.example.kameonlib.utils.Constants.baseUrl
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private var mAdapter: StorageAdapter? = null
    var getStorageList: List<GetStorageModel>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
        callApi()
        if (isOnline(this)) {
            btn_upl_img.setOnClickListener {
                Permissions.check(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    null,
                    object : PermissionHandler() {
                        override fun onGranted() {
                            openGallery(this@MainActivity)
                        }
                    })
            }
            btn_upl_file.setOnClickListener {
                Permissions.check(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    null,
                    object : PermissionHandler() {
                        override fun onGranted() {
                            openFiles(this@MainActivity)
                        }
                    })
            }

        } else {
            showToastMsg()
        }

    }


    /*****************************
     * Setting adapter
     * ****************************/

    private fun setViews() {
        mAdapter = StorageAdapter()
        rv_folder.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_folder.adapter = mAdapter
    }

    /*****************************
     * Calling API for created folder
     * ****************************/
    private fun callApi() {
        if (isOnline(this)) {
            val getStorageRes = ApiClient.getInstance(baseUrl).create(ApiInterface::class.java)
            // launching a new coroutine
            CoroutineScope(Dispatchers.IO).launch {
                val result = getStorageRes.getStorageFolder()
                if (result.isSuccessful) {
                    getStorageList = result.body()
                    withContext(Dispatchers.Main) {
                        cl_show_progress.visibility = View.GONE
                        getStorageList?.let { mAdapter?.setData(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.some_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                        cl_show_progress.visibility = View.GONE
                    }
                }
            }
        } else {
            showToastMsg()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {


            GALLERY_PICTURE -> {
                tv_upload_data.text = getString(R.string.uploading)
                callForUploadFileImages(cl_show_progress, data, "image/*")
            }

            PICKFILE_REQUEST_CODE -> {
                tv_upload_data.text = getString(R.string.uploading)
                callForUploadFileImages(cl_show_progress, data, "1")

            }

        }


    }

}