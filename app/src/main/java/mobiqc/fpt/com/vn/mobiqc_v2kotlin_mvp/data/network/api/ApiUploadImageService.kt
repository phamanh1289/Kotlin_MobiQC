package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiUploadImageService {
    @Multipart
    @POST("upload")
    fun postUploadImage(@Header("Authorization") token: String, @Part file: MultipartBody.Part): Call<ResponseBody>
}