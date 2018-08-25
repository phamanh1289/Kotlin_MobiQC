package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.image.upload_image.diff

import android.support.v7.util.DiffUtil
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.UploadImageModel

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