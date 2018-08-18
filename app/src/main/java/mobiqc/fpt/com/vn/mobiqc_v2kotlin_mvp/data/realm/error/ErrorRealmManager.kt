package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error

import io.realm.ErrorRealmModelRealmProxy
import io.realm.Realm
import io.realm.RealmResults
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorDataModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants


/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class ErrorRealmManager {

    companion object {
        const val ERROR_COL_ID = "id"
        const val ERROR_COL_DEPARTMENT = "department"
        const val ERROR_COL_TYPE = "type"
        const val ERROR_COL_MAIN = "main"
//        val ERROR_INF_TABLE = "error_infrastructure"
//        val ERROR_INF_COL_ID = "id"
//        val ERROR_INF_COL_TYPE = "type"
//        val ERROR_INF_COL_DESC = "description"
//        val ERROR_INF_COL_CREATEDATE = "createdate"
//        val PARTNER_TABLE = "partner"
//        val PARTNER_COL_ZONE = "zone"
//        val PARTNER_COL_BRANCH = "branch"
//        val PARTNER_COL_PARTNER = "partner"
//        val PARTNER_COL_EMAIL = "email"
//        val PARTNER_COL_CREATEDATE = "createdate"
    }

    private var realm: Realm? = null

    fun getCountError(): Int {
        var count: Int? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                count = it.where(ErrorRealmModel::class.java)?.findAll()?.size
            }
        } finally {
            realm?.close()
        }
        return count ?: 0
    }

    fun insertError(mode: ErrorDataModel) {
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                val item = it.createObject(ErrorRealmModel::class.java, mode.id)
                item?.date = mode.date
                item?.department = mode.department
                item?.description = mode.description
                item?.type = mode.type
                item?.main = mode.main
                it.insertOrUpdate(item!!)
            }
        } finally {
            realm?.close()
        }
    }

    fun updateError(mode: ErrorDataModel) {
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                val item = it.where(ErrorRealmModel::class.java)?.equalTo("id", mode.id)?.findFirst()
                item?.date = mode.date
                item?.department = mode.department
                item?.description = mode.description
                item?.type = mode.type
                item?.main = mode.main
                it.copyToRealmOrUpdate(item)
            }
        } finally {
            realm?.close()
        }
    }

    fun findIdError(id: Int): Boolean {
        try {
            realm = Realm.getDefaultInstance()
            var item: ErrorRealmModel? = null
            realm?.executeTransaction {
                item = it.where(ErrorRealmModel::class.java)?.equalTo(ERROR_COL_ID, id)?.findFirst()
            }
            return item != null
        } finally {
            realm?.close()
        }
    }

    fun getDistinctDepartment(): ArrayList<SingleChoiceModel> {
        try {
            realm = Realm.getDefaultInstance()
            val list = ArrayList<SingleChoiceModel>()
            var result: RealmResults<ErrorRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(ErrorRealmModel::class.java)?.distinct(ERROR_COL_DEPARTMENT)
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as ErrorRealmModelRealmProxy).`realmGet$department`(), status = i == Constants.FIRST_ITEM))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }

    fun getDistinctTypeError(department: String): ArrayList<SingleChoiceModel> {
        try {
            val list = ArrayList<SingleChoiceModel>()
            list.add(SingleChoiceModel(account = Constants.ERROR_FIRST_ITEM, status = true))
            realm = Realm.getDefaultInstance()
            var result: RealmResults<ErrorRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(ErrorRealmModel::class.java)?.equalTo(ERROR_COL_DEPARTMENT, department)?.distinct(ERROR_COL_TYPE)
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as ErrorRealmModelRealmProxy).`realmGet$type`()))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }

    fun getDistinctMainError(department: String, type: String, typeCheck: Boolean): ArrayList<SingleChoiceModel> {
        try {
            val list = ArrayList<SingleChoiceModel>()
            if (!typeCheck) {
                realm = Realm.getDefaultInstance()
                var result: RealmResults<ErrorRealmModel>? = null
                realm?.executeTransaction {
                    result = it.where(ErrorRealmModel::class.java)?.equalTo(ERROR_COL_DEPARTMENT, department)?.equalTo(ERROR_COL_TYPE, type)?.distinct(ERROR_COL_MAIN)
                }
                result?.let {
                    if (it.size != 0) {
                        for (i in 0 until it.size) {
                            list.add(SingleChoiceModel(account = (it[i] as ErrorRealmModelRealmProxy).`realmGet$main`(), status = i == 0))
                        }
                    }
                }
            } else list.add(SingleChoiceModel(account = Constants.ERROR_FIRST_ITEM, status = true))
            return list
        } finally {
            realm?.close()
        }
    }

    fun getDistinctDescription(department: String, type: String, main: String, typeCheck: Boolean): ArrayList<SingleChoiceModel> {
        try {
            val list = ArrayList<SingleChoiceModel>()
            if (!typeCheck) {
                realm = Realm.getDefaultInstance()
                var result: RealmResults<ErrorRealmModel>? = null
                realm?.executeTransaction {
                    result = it.where(ErrorRealmModel::class.java)?.equalTo(ERROR_COL_DEPARTMENT, department)?.equalTo(ERROR_COL_TYPE, type)?.equalTo(ERROR_COL_MAIN, main)?.findAll()
                }
                result?.let {
                    if (it.size != 0) {
                        for (i in 0 until it.size) {
                            list.add(SingleChoiceModel(account = (it[i] as ErrorRealmModelRealmProxy).`realmGet$description`(), status = i == 0, id = (it[i] as ErrorRealmModelRealmProxy).`realmGet$id`()))
                        }
                    }
                }
            } else list.add(SingleChoiceModel(account = Constants.ERROR_FIRST_ITEM, status = true))
            return list
        } finally {
            realm?.close()
        }
    }

}