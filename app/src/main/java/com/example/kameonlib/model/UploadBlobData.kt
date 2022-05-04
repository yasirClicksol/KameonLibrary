package com.example.kameonlib.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class UploadBlobData {
    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("detail")
    @Expose
    var detail: String? = null

    @SerializedName("instance")
    @Expose
    var instance: String? = null

    @SerializedName("additionalProp1")
    @Expose
    var additionalProp1: String? = null

    @SerializedName("additionalProp2")
    @Expose
    var additionalProp2: String? = null

    @SerializedName("additionalProp3")
    @Expose
    var additionalProp3: String? = null
}