package com.example.kameonlib.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Properties {
    @SerializedName("foo")
    @Expose
    var foo: Foo? = null

    @SerializedName("bar")
    @Expose
    var bar: Bar? = null

    @SerializedName("baz")
    @Expose
    var baz: Baz? = null
}