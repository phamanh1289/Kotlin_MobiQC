package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error

import io.reactivex.Observable
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorDataModel

/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface ErrorRealmContract {

    fun insertError(mode: ErrorDataModel)

    fun updateError(mode: ErrorDataModel)

    fun findIdError(id: Int): Boolean

    fun getAllError() : Observable<MutableList<ErrorDataModel>>

}