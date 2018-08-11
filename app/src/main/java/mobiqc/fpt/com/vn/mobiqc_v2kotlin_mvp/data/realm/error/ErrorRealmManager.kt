package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.error

import io.reactivex.Observable
import io.realm.Realm
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ErrorDataModel
import java.util.*

/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class ErrorRealmManager : ErrorRealmContract {


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

    override fun insertError(mode: ErrorDataModel) {
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

    override fun updateError(mode: ErrorDataModel) {
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

    override fun findIdError(id: Int): Boolean {
        try {
            realm = Realm.getDefaultInstance()
            var item: ErrorRealmModel? = null
            realm?.executeTransaction {
                item = it.where(ErrorRealmModel::class.java)?.equalTo("id", id)?.findFirst()
            }
            return item != null
        } finally {
            realm?.close()
        }
    }

    override fun getAllError(): Observable<MutableList<ErrorDataModel>> {
        val list: MutableList<ErrorDataModel> = ArrayList()
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                it.where(ErrorRealmModel::class.java)?.findAll()?.forEach { item ->
                    list.add(ErrorDataModel(id = item.id, date = item.date, department = item.department, description = item.description, main = item.main, type = item.type))
                }
            }
        } finally {
            realm?.close()
        }
        return Observable.just(list)
    }

//    override fun deleteAlbum(model: AlbumModel): Observable<Boolean> {
//        try {
//            var isCheck = false
//            realm = Realm.getDefaultInstance()
//            realm?.executeTransaction {
//                val result = it.where(AlbumRealmModel::class.java)?.equalTo("id", model.id)?.findAll()
//                isCheck = result?.size != 0
//                if (isCheck) result?.deleteAllFromRealm()
//            }
//            return Observable.just(isCheck)
//        } finally {
//            realm?.close()
//        }
//    }
}