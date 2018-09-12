package vn.com.fpt.mobiqc.data.interfaces

/**
 * * Created by Anh Pham on 08/03/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
interface MenuCheckListDialogInterface {
    fun onClickError(index: Int)
    fun onClickDetail(index: Int)
    fun onClickUpdateDetail(index: Int)
    fun onClickUpdateStatus(index: Int)
}