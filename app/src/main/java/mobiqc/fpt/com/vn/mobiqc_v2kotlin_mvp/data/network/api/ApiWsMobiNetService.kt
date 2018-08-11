package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiWsMobiNetService {

    @POST("Deployment_GetGroupInfo")
    fun getDeploymentContractGroup(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("Deployment_GetContractList")
    fun getDeploymentContractList(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("Deployment_GetContractDetail")
    fun getDeploymentContractDetail(@Body map: HashMap<String, Any>): Observable<ResponseModel>

}