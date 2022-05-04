package com.example.kameonlib.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kameonlib.R
import com.example.kameonlib.adapter.UploadedBlobAdapter
import com.example.kameonlib.model.DeleteBlobById
import com.example.kameonlib.model.GetUploadedDataResponse
import com.example.kameonlib.network.ApiClient
import com.example.kameonlib.network.ApiInterface
import com.example.kameonlib.utils.Constants
import com.example.kameonlib.utils.Constants.RESPONSE_DATA
import com.example.kameonlib.utils.Constants.baseUrl
import com.example.kameonlib.utils.Constants.onMoreBtnClicked
import com.example.kameonlib.utils.isOnline
import com.example.kameonlib.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_detail_preview.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.cl_show_progress
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.activity_preview.backBtn
import kotlinx.android.synthetic.main.dlg_alert_delete.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PreviewActivity : AppCompatActivity() {
    private var mAdapter: UploadedBlobAdapter? = null
    var filteredList: ArrayList<GetUploadedDataResponse> = ArrayList()
    var fileName: String? = ""
    var getBlobList: List<GetUploadedDataResponse>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        intent?.apply {
            fileName = getStringExtra(Constants.FILE_NAME)

        }

        /*****************************
         * Applying search filter
         * ****************************/


        searchView?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {

                    filteredList = ArrayList()
                    if (p0.toString() != "") {
                        for (item in getBlobList!!) {
                            if (item.fileName?.toLowerCase(Locale.ROOT)?.contains(
                                    p0.toString().toLowerCase(
                                        Locale.ROOT
                                    )
                                )!!
                            ) {
                                filteredList.add(item)
                            }

                        }
                        mAdapter?.setData(filteredList)
                    } else {
                        mAdapter?.setData(getBlobList!!)
                    }

                } else {
                    filteredList.clear()
                    getBlobList?.let { mAdapter?.setData(it) }
                }

            }

        })
        setViews()
        callApiForPreview()
        title_toolbarP.text = fileName
        backBtn.setOnClickListener { onBackPressed() }
        onMoreBtnClicked = { pos: Int, id: String?, view: ImageView ->
            popupMenu(pos, id, view)

        }

    }


    /*****************************
     * inflating popup menu
     * ****************************/

    private fun popupMenu(pos: Int, id: String?, view: ImageView) {
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.popup_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete -> {
                        showDeleteDialog(pos, id)
                        true
                    }
                    R.id.view_detail -> {
                        val intent = Intent(this@PreviewActivity, DetailPreviewActivity::class.java)
                        intent.putExtra(RESPONSE_DATA, getBlobList?.get(pos))
                        startActivity(intent)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }.show()

    }

    /*****************************
     * delete item dialog
     * ****************************/

    private fun showDeleteDialog(pos: Int, id: String?) {

        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dlg_alert_delete, null)
        val mBottomSheetDialog = Dialog(
            this,
            R.style.CustomizeDialog
        )
        mBottomSheetDialog.setContentView(dialogView)
        mBottomSheetDialog.setCancelable(true)
        mBottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        mBottomSheetDialog.apply {
            btn_ok_pdf.setOnClickListener {
                deleteApi(pos, id)
                dismiss()
            }
            btn_cancel_pdf.setOnClickListener { mBottomSheetDialog.dismiss(); }
        }
        mBottomSheetDialog.show()
        mBottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /*****************************
     * Setting adapter
     * ****************************/

    private fun setViews() {
        mAdapter = UploadedBlobAdapter()
        rv_blob_folder.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_blob_folder.adapter = mAdapter
    }


    /*****************************
     * call api for callApiForPreview
     * ****************************/

    private fun callApiForPreview() {
        if (isOnline(this)) {
            cl_show_progress.visibility = View.VISIBLE
            val getUploadedRes = ApiClient.getInstance(baseUrl).create(ApiInterface::class.java)
            // launching a new coroutine
            CoroutineScope(Dispatchers.IO).launch {
                val result = getUploadedRes.getBlobFolder()
                if (result.isSuccessful) {
                    getBlobList = result.body()
                    withContext(Dispatchers.Main) {
                        cl_show_progress.visibility = View.GONE
                        getBlobList?.let { mAdapter?.setData(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PreviewActivity,
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
    /*****************************
     * call api for delete item
     * ****************************/
    private fun deleteApi(pos: Int, id: String?) {
        tv_upload_dataP.text = getString(R.string.delete)
        cl_show_progress.visibility = View.VISIBLE
        val getStorageRes = ApiClient.getInstance(baseUrl).create(ApiInterface::class.java)
        // launching a new coroutine
        CoroutineScope(Dispatchers.IO).launch {
            val result = id?.let { it1 -> getStorageRes.deleteItem(it1) }
            result?.enqueue(object : Callback<DeleteBlobById?> {
                override fun onResponse(
                    call: Call<DeleteBlobById?>,
                    response: Response<DeleteBlobById?>
                ) {
                    if (response.isSuccessful) {
                        mAdapter?.notifyItemRemoved(pos)
                        cl_show_progress.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<DeleteBlobById?>, t: Throwable) {
                }

            })

        }


    }
}