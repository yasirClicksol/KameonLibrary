package com.example.kameonlib.network


import com.example.kameonlib.model.DeleteBlobById
import com.example.kameonlib.model.GetStorageModel
import com.example.kameonlib.model.GetUploadedDataResponse
import com.example.kameonlib.model.UploadBlobData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

import retrofit2.http.POST

import retrofit2.http.DELETE
import okhttp3.RequestBody

import okhttp3.MultipartBody

import retrofit2.http.Multipart
import okhttp3.ResponseBody
import java.util.*


interface ApiInterface {

    @GET("StorageContainers")
    suspend fun getStorageFolder() : Response<List<GetStorageModel>>

    @GET("Blobs")
    suspend fun getBlobFolder() : Response<List<GetUploadedDataResponse>>



    @Multipart
    @POST("Blobs")
    fun uploadBlobDataServer(
        @Part("StorageContainerId") containerID: RequestBody?,
        @Part file : MultipartBody.Part?,
        @Part("CreatedBy") createdBy: RequestBody?,
        @Part("CreationSource") creationSource: RequestBody?
    ): Call<String?>?

    @DELETE("Blobs/{id}")
    fun deleteItem(@Path("id") itemId: String): Call<DeleteBlobById?>?

    /*@GET("{code}/rates.json")
    suspend fun getReteByCode(
        @Path("code") str: String?
    ): Response<Exchanges>?*/

}