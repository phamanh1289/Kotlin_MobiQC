package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model

import com.google.gson.Gson
import okhttp3.Response

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class ErrorServerModel {

    var message: String? = null
        get() = field ?: "Error from server, Please try again!"
    var code = -1

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {

        fun parseData(json: String): ErrorServerModel {
            var errorModel: ErrorServerModel
            try {
                val gson = Gson()
                errorModel = gson.fromJson(json, ErrorServerModel::class.java)
                if (errorModel != null) return errorModel
                errorModel = ErrorServerModel()
                errorModel.message = json
            } catch (e: Exception) {
                errorModel = ErrorServerModel()
                errorModel.message = json
            }
            return errorModel
        }

        fun getErrorString(response: Response): String {
            val errorModel = ErrorServerModel()
            errorModel.code = response.code()
            errorModel.message = if (!response.body()!!.toString().startsWith("<!DOCTYPE HTML")) response.body()!!.toString() else ""
            return errorModel.toString()
        }
    }
}