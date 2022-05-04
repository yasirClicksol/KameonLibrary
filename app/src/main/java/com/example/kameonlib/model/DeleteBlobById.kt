package com.example.kameonlib.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class DeleteBlobById {
    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("properties")
    @Expose
    var properties: Properties? = null
}