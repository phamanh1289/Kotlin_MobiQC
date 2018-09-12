package vn.com.fpt.mobiqc.data.network.model

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
data class AccountGroup (var group : String = "", var accounts : ArrayList<SingleChoiceModel>, var status : Boolean = false) :BaseModel()