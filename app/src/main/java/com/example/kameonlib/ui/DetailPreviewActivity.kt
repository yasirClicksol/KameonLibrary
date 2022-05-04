package com.example.kameonlib.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.kameonlib.R
import com.example.kameonlib.model.GetUploadedDataResponse
import com.example.kameonlib.utils.Constants.CONTENT_TYPE
import com.example.kameonlib.utils.Constants.FILE_NAME
import com.example.kameonlib.utils.Constants.IMAGE_URL
import com.example.kameonlib.utils.Constants.RESPONSE_DATA
import kotlinx.android.synthetic.main.activity_detail_preview.*
import java.io.File


class DetailPreviewActivity : AppCompatActivity() {
    private var getResponseBlobList: GetUploadedDataResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_preview)

       /* getting intent data*/
        intent?.apply {
            getResponseBlobList = intent.getParcelableExtra(RESPONSE_DATA)

        }
        title_toolbar.text = getResponseBlobList?.fileName
        iv_preview_img.setOnClickListener {
            val intent = Intent(this, ViewImageActivity::class.java)
            intent.putExtra(IMAGE_URL, getResponseBlobList?.previewUrl)
            intent.putExtra(CONTENT_TYPE, getResponseBlobList?.contentType)
            intent.putExtra(FILE_NAME, getResponseBlobList?.fileName)
            startActivity(intent)

        }
        if (getResponseBlobList?.contentType?.contains("image")!!) {

            Glide.with(this)
                .load(getResponseBlobList?.previewUrl)
                .thumbnail(.5f)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                    ).diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(iv_preview_img)
        } else {
            iv_preview_img.setImageResource(R.drawable.ic_folder)
        }
        backBtn.setOnClickListener { onBackPressed() }

        tv_createdby_name.text = getResponseBlobList?.createdBy
        tv_name.text = getResponseBlobList?.fileName
        title_toolbar.text = getResponseBlobList?.fileName
        tv_desc.text = getResponseBlobList?.description

    }
}