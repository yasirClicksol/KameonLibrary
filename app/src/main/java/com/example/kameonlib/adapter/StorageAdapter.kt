package com.example.kameonlib.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kameonlib.R
import com.example.kameonlib.model.GetStorageModel
import com.example.kameonlib.ui.PreviewActivity
import com.example.kameonlib.utils.Constants.FILE_NAME


class StorageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var getResponseList: List<GetStorageModel> = ArrayList()


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.li_storage_folder, parent, false)
        return ItemHolder(inflate)
    }

    override fun getItemCount(): Int {
        return getResponseList.size
    }


    fun setData(modelList: List<GetStorageModel>) {
        this.getResponseList = modelList
        notifyDataSetChanged()
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var folderName: TextView = itemView.findViewById(R.id.tv_folder_name)
        internal var container: ConstraintLayout = itemView.findViewById(R.id.cl_container)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList = getResponseList[position]
        (holder as ItemHolder).folderName.text = dataList.name
        holder.container.setOnClickListener {
           val intent = Intent(holder.itemView.context , PreviewActivity::class.java)
            intent.putExtra(FILE_NAME, dataList.name)
            holder.itemView.context.startActivity(intent)
        }


    }
}