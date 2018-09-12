package vn.com.fpt.mobiqc.data.realm.location

import io.realm.LocationRealmModelRealmProxy
import io.realm.Realm
import io.realm.RealmResults
import vn.com.fpt.mobiqc.data.network.model.LocationUserModel
import vn.com.fpt.mobiqc.data.network.model.SingleChoiceModel
import vn.com.fpt.mobiqc.others.constant.Constants


/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class LocationRealmManager {

    companion object {
        const val LOCATION_COL_PARENT = "parentdesc"
    }

    private var realm: Realm? = null

    fun insertLocation(mode: LocationUserModel) {
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                val item = it.createObject(LocationRealmModel::class.java, mode.id)
                item?.namedesc = mode.namedesc
                item?.parentdesc = mode.parentdesc
                item?.let { model -> it.insertOrUpdate(model) }
            }
        } finally {
            realm?.close()
        }
    }

    fun deleteAllLocation() {
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                val result = it.where(LocationRealmModel::class.java).findAll()
                result.deleteAllFromRealm()
            }
        } finally {
            realm?.close()
        }
    }

    fun getDistinctNameDesc(parentDesc: String): ArrayList<SingleChoiceModel> {
        try {
            realm = Realm.getDefaultInstance()
            val list = ArrayList<SingleChoiceModel>()
            var result: RealmResults<LocationRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(LocationRealmModel::class.java)?.equalTo(LOCATION_COL_PARENT, parentDesc)?.findAll()
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as LocationRealmModelRealmProxy).`realmGet$namedesc`(), status = i == Constants.FIRST_ITEM))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }

    fun getDistinctParent(): ArrayList<SingleChoiceModel> {
        try {
            val list = ArrayList<SingleChoiceModel>()
            realm = Realm.getDefaultInstance()
            var result: RealmResults<LocationRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(LocationRealmModel::class.java)?.distinct(LOCATION_COL_PARENT)
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as LocationRealmModelRealmProxy).`realmGet$parentdesc`(), status = i == Constants.FIRST_ITEM, id = (it[i] as LocationRealmModelRealmProxy).`realmGet$id`()))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }
}