package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiUploadImageService {
    @Multipart
    @POST("upload")
    @Headers("Content-Type: application/json")
    fun postUploadImage(@Header("Authorization") token: String, @Part("fileType") file: RequestBody): Observable<ResponseModel>
}