package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.diff

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image_choice.view.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImageAdapter(val onClick: (Int) -> Unit) : ListAdapter<UploadImageModel, UploadImageAdapter.UploadImageHolder>(UploadImageDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageAdapter.UploadImageHolder {
        return UploadImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_choice, parent, false))
    }

    override fun onBindViewHolder(holder: UploadImageHolder, position: Int) {
        holder.bindData(getItem(position), onClick)
    }

    inner class UploadImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: UploadImageModel?, onClick: (Int) -> Unit) {
            model?.let { item ->
                Glide.with(itemView.context)
                        .load(item.filePath)
                        .override(350, 550).centerCrop()
                        .into(itemView.itemImageChoice_imgPicture)
                itemView.itemImageChoice_imgDelete.setOnClickListener {
                    onClick(adapterPosition)
                }
            }
        }
    }
}