package com.example.kameonlib.utils

import android.widget.ImageView

/*****************************
 * Setting global constants
 * ****************************/

object Constants {
    const val GALLERY_PICTURE = 1
    const val PICKFILE_REQUEST_CODE = 2
    const val RESPONSE_DATA = "data"
    const val IMAGE_URL = "image_url"
    const val CONTENT_TYPE = "type"
    const val FILE_NAME = "name"
    val baseUrl = "http://app-bbg-blob-assessment.azurewebsites.net/BlobStorage/"
    var onMoreBtnClicked: ((pos: Int, id: String? , view: ImageView) -> Unit)? = null

}