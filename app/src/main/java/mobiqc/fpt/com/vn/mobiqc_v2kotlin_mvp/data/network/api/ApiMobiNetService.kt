package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ApiMobiNetService {
    @POST("Maintain_GetGroupInfo")
    fun getMaintenanceContractGroup(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("Maintain_GetContractList")
    fun getMaintenanceContractList(@Body map: HashMap<String, Any>): Observable<ResponseModel>

    @POST("Maintain_GetContractDetail")
    fun getMaintenanceContractDetail(@Body map: HashMap<String, Any>): Observable<ResponseModel>

}