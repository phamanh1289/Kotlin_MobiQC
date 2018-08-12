package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseAccountGroupModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiService {

    @POST("Login")
    fun postLogin(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetMobiAccountByGroup")
    fun getMobiAccount(@Body map: HashMap<String, Any>): Observable<ResponseAccountGroupModel>

    @POST("CheckImei")
    fun postCheckImei(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("CheckAppVersion")
    fun getAppVersion(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetFinishedContractList")
    fun getCompletedContract(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetNewErrorData")
    fun getNewErrorData(@Body map: HashMap<String, Any>): Observable<ResponseErrorDataModel>

    @POST("GetChecklistDetail")
    fun getCheckListDetail(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetFinishedContractDetail")
    fun getFinishContractDetail(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetDepositsContract")
    fun getDepositsContract(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetCoordinateContract")
    fun getCoordinateContract(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("GetAllPhoneNumber")
    fun getAllPhoneNumber(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @GET
    @Streaming
    fun getFileNewVersion(@Url fileUrl: String): Call<ResponseBody>
}