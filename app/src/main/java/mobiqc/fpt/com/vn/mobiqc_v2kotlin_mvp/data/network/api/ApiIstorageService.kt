package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import retrofit2.http.*

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiIstorageService {
    @POST("auth/partner/getAccessToken")
    fun getAccessToken(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("iqc/partner/createImage")
    fun postCreateImage(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("iqc/partner/album")
    fun postAlbum(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("iqc/partner/deleteImage")
    fun postDeleteImage(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("iqc/partner/addImage")
    fun postAddImage(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @GET("iqc/partner/album")
    fun getAlbum(@Header("Authorization") token : String, @Query("Code") code: String): Observable<ResponseModel>

}