package vn.com.fpt.mobiqc.data.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiUploadImageService {
//    @Multipart
    @POST("upload")
    fun postUploadImage(@Header("Authorization") token: String
//                        , @Part map: MultipartBody.Part
    ): Observable<ResponseBody>
}