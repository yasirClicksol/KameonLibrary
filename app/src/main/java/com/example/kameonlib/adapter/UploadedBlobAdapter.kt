package com.example.kameonlib.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.kameonlib.R
import com.example.kameonlib.model.DeleteBlobById
import com.example.kameonlib.model.GetStorageModel
import com.example.kameonlib.model.GetUploadedDataResponse
import com.example.kameonlib.model.UploadBlobData
import com.example.kameonlib.network.ApiClient
import com.example.kameonlib.network.ApiInterface
import com.example.kameonlib.ui.DetailPreviewActivity
import com.example.kameonlib.ui.PreviewActivity
import com.example.kameonlib.utils.Constants.RESPONSE_DATA
import com.example.kameonlib.utils.Constants.onMoreBtnClicked
import com.example.kameonlib.utils.callApiUploadData
import com.example.kameonlib.utils.isOnline
import com.example.kameonlib.utils.showToastMsg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadedBlobAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var getResponseBlobList: List<GetUploadedDataResponse> = ArrayList()


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.li_blob_layout, parent, false)
        return ItemHolder(inflate)
    }

    override fun getItemCount(): Int {
        return getResponseBlobList.size
    }


    fun setData(modelList: List<GetUploadedDataResponse>) {
        this.getResponseBlobList = modelList
        notifyDataSetChanged()
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var folderName: TextView = itemView.findViewById(R.id.tv_folder_name)
        internal var displayImage: ImageView = itemView.findViewById(R.id.iv_display_img)
        internal var container: ConstraintLayout = itemView.findViewById(R.id.cl_display_img)
        internal var options: ImageView = itemView.findViewById(R.id.more_option)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList = getResponseBlobList[position]
        (holder as ItemHolder).folderName.text = dataList.fileName
        if (dataList.contentType?.contains("image")!!) {
            Glide.with(holder.itemView.context)
                .load(dataList.previewUrl)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        GranularRoundedCorners(16f, 16f, 0f, 0f)
                    )
                )
                .into(holder.displayImage)
        } else {
            holder.displayImage.setImageResource(R.drawable.ic_folder)
        }
        holder.options.setOnClickListener {
            onMoreBtnClicked?.invoke(position , dataList.id , holder.options)
        }
        holder.container.setOnClickListener {

            val intent = Intent(holder.itemView.context , DetailPreviewActivity::class.java)
            intent.putExtra(RESPONSE_DATA , dataList)
            holder.itemView.context.startActivity(intent)

        }


    }
}