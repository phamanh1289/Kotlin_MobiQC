package vn.com.fpt.mobiqc.ui.image.upload_image.diff

import android.support.v7.util.DiffUtil
import vn.com.fpt.mobiqc.data.network.model.UploadImageModel

/**
 * * Created by Anh Pham on 08/08/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class UploadImageDiff : DiffUtil.ItemCallback<UploadImageModel>() {
    override fun areItemsTheSame(oldItem: UploadImageModel, newItem: UploadImageModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UploadImageModel, newItem: UploadImageModel): Boolean {
        return oldItem == newItem
    }

}