package com.example.kameonlib.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Foo {
    @SerializedName("type")
    @Expose
    var type: String? = null
}