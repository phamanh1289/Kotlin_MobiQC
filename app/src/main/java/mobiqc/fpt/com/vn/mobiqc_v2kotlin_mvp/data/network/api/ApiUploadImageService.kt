package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiUploadImageService {
    @Multipart
    @POST("upload.php")
    @Headers("Content-Type: application/json")
    fun postUploadImage(@Header("Authorization") token:String, @Part file: MultipartBody.Part): Observable<ResponseModel>

    @Multipart
    @POST("upload.php")
//    @Headers("Content-Type: application/json")
    fun postUploadImageDemo(@Part file: MultipartBody.Part): Observable<ResponseModel>
}