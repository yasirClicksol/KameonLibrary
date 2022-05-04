package com.example.kameonlib.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class GetStorageModel {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("storageType")
    @Expose
    var storageType: String? = null

    @SerializedName("maxFileSize")
    @Expose
    var maxFileSize: Int? = null
}