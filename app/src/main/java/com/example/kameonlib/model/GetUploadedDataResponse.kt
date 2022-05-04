package com.example.kameonlib.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class GetUploadedDataResponse() : Parcelable {
    @SerializedName("downloadUrl")
    @Expose
    var downloadUrl: String? = null

    @SerializedName("previewUrl")
    @Expose
    var previewUrl: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("fileName")
    @Expose
    var fileName: String? = null

    @SerializedName("filesize")
    @Expose
    var filesize: Int? = null

    @SerializedName("contentType")
    @Expose
    var contentType: String? = null

    @SerializedName("createdBy")
    @Expose
    var createdBy: String? = null

    @SerializedName("createdTimestamp")
    @Expose
    var createdTimestamp: String? = null

    @SerializedName("creationSource")
    @Expose
    var creationSource: String? = null

    @SerializedName("domainIdentityType")
    @Expose
    var domainIdentityType: String? = null

    @SerializedName("domainIdentityValue")
    @Expose
    var domainIdentityValue: String? = null

    @SerializedName("tags")
    @Expose
    var tags: List<String>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    constructor(parcel: Parcel) : this() {
        downloadUrl = parcel.readString()
        previewUrl = parcel.readString()
        id = parcel.readString()
        fileName = parcel.readString()
        filesize = parcel.readValue(Int::class.java.classLoader) as? Int
        contentType = parcel.readString()
        createdBy = parcel.readString()
        createdTimestamp = parcel.readString()
        creationSource = parcel.readString()
        domainIdentityType = parcel.readString()
        domainIdentityValue = parcel.readString()
        tags = parcel.createStringArrayList()
        description = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(downloadUrl)
        parcel.writeString(previewUrl)
        parcel.writeString(id)
        parcel.writeString(fileName)
        parcel.writeValue(filesize)
        parcel.writeString(contentType)
        parcel.writeString(createdBy)
        parcel.writeString(createdTimestamp)
        parcel.writeString(creationSource)
        parcel.writeString(domainIdentityType)
        parcel.writeString(domainIdentityValue)
        parcel.writeStringList(tags)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetUploadedDataResponse> {
        override fun createFromParcel(parcel: Parcel): GetUploadedDataResponse {
            return GetUploadedDataResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetUploadedDataResponse?> {
            return arrayOfNulls(size)
        }
    }

}