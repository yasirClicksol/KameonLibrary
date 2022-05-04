package com.example.kameonlib.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.kameonlib.R
import com.example.kameonlib.utils.Constants
import com.example.kameonlib.utils.Constants.CONTENT_TYPE
import com.example.kameonlib.utils.Constants.FILE_NAME
import com.example.kameonlib.utils.Constants.IMAGE_URL
import kotlinx.android.synthetic.main.activity_detail_preview.*
import kotlinx.android.synthetic.main.activity_view_image.*
import kotlinx.android.synthetic.main.activity_view_image.backBtn

class ViewImageActivity : AppCompatActivity() {
    var imagePath: String? = ""
    var contentType: String? = ""
    var fileName: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        intent?.apply {
            imagePath = getStringExtra(IMAGE_URL)
            contentType = getStringExtra(CONTENT_TYPE)
            fileName = getStringExtra(FILE_NAME)

        }
        title_toolbaImgr.text = fileName
        backBtn.setOnClickListener { onBackPressed() }

        if (contentType?.contains("image")!!){
            Glide.with(this)
                .load(imagePath)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                    )
                )
                .into(iv_view_img)
        }
        else{
            if (contentType?.contains("application/pdf")!!){
                /*need library to show*/
                no_preview.visibility = View.VISIBLE
            }
        }

    }

}