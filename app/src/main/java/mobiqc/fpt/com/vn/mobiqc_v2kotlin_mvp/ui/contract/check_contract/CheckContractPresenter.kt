package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.check_contract

import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.api.ApiService
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BasePresenter
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class CheckContractPresenter @Inject constructor(val apiService: ApiService): BasePresenter<CheckContract.CheckContractView>(), CheckContract.CheckContractPresenter {
}