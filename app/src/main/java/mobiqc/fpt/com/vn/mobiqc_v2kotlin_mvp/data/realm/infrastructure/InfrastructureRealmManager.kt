package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.infrastructure

import android.content.res.Resources
import io.realm.InfrastructureRealmModelRealmProxy
import io.realm.Realm
import io.realm.RealmResults
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants


/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class InfrastructureRealmManager {

    companion object {
        const val INFRASTRUC_TYPE = "type"
    }

    private var realm: Realm? = null

    fun importFromJson(resources: Resources) {
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                val inputStream = resources.openRawResource(R.raw.error_infrastructure)
                it.createAllFromJson(InfrastructureRealmModel::class.java, inputStream)
            }
        } finally {
            realm?.close()
        }
    }

    fun getCountInfrast(): Int {
        var count: Int? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                count = it.where(InfrastructureRealmModel::class.java)?.findAll()?.size
            }
        } finally {
            realm?.close()
        }
        return count ?: 0
    }

    fun getDescription(type: String): ArrayList<SingleChoiceModel> {
        try {
            realm = Realm.getDefaultInstance()
            val list = ArrayList<SingleChoiceModel>()
            var result: RealmResults<InfrastructureRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(InfrastructureRealmModel::class.java)?.equalTo(INFRASTRUC_TYPE, type)?.findAll()
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(id = (it[i] as InfrastructureRealmModelRealmProxy).`realmGet$id`(), account = (it[i] as InfrastructureRealmModelRealmProxy).`realmGet$description`(), status = i == Constants.FIRST_ITEM))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }
}