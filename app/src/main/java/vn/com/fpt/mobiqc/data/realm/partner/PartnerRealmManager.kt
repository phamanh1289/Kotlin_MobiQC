package vn.com.fpt.mobiqc.data.realm.partner

import android.content.res.Resources
import io.realm.PartnerRealmModelRealmProxy
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.SingleChoiceModel
import vn.com.fpt.mobiqc.others.constant.Constants


/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class PartnerRealmManager {

    companion object {
        const val PARTNER_COL_ZONE = "zone"
        const val PARTNER_COL_BRANCH = "branch"
        const val PARTNER_COL_PARTNER = "partner"
        const val PARTNER_COL_EMAIL = "email"
    }

    private var realm: Realm? = null

    fun importFromJson(resources: Resources) {
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                val inputStream = resources.openRawResource(R.raw.partner)
                it.createAllFromJson(PartnerRealmModel::class.java, inputStream)
            }
        } finally {
            realm?.close()
        }
    }

    fun getCountPartner(): Int {
        var count: Int? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction {
                count = it.where(PartnerRealmModel::class.java)?.findAll()?.size
            }
        } finally {
            realm?.close()
        }
        return count ?: 0
    }

    fun getZone(): ArrayList<SingleChoiceModel> {
        try {
            realm = Realm.getDefaultInstance()
            val list = ArrayList<SingleChoiceModel>()
            var result: RealmResults<PartnerRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(PartnerRealmModel::class.java)?.distinct(PARTNER_COL_ZONE)?.sort(PARTNER_COL_ZONE, Sort.ASCENDING)
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as PartnerRealmModelRealmProxy).`realmGet$zone`(), status = i == Constants.FIRST_ITEM))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }

    fun getBranch(zone: String): ArrayList<SingleChoiceModel> {
        try {
            realm = Realm.getDefaultInstance()
            val list = ArrayList<SingleChoiceModel>()
            var result: RealmResults<PartnerRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(PartnerRealmModel::class.java)?.equalTo(PARTNER_COL_ZONE, zone)?.distinct(PARTNER_COL_BRANCH)
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as PartnerRealmModelRealmProxy).`realmGet$branch`(), status = i == Constants.FIRST_ITEM))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }

    fun getPartner(zone: String, branch: String): ArrayList<SingleChoiceModel> {
        try {
            realm = Realm.getDefaultInstance()
            val list = ArrayList<SingleChoiceModel>()
            var result: RealmResults<PartnerRealmModel>? = null
            realm?.executeTransaction {
                result = it.where(PartnerRealmModel::class.java)?.equalTo(PARTNER_COL_ZONE, zone)?.equalTo(PARTNER_COL_BRANCH, branch)?.distinct(PARTNER_COL_PARTNER)
            }
            result?.let {
                if (it.size != 0) {
                    for (i in 0 until it.size) {
                        list.add(SingleChoiceModel(account = (it[i] as PartnerRealmModelRealmProxy).`realmGet$partner`(), status = i == Constants.FIRST_ITEM))
                    }
                }
            }
            return list
        } finally {
            realm?.close()
        }
    }

    fun getEmail(zone: String, branch: String, pratner: String): String? {
        try {
            realm = Realm.getDefaultInstance()
            var item: PartnerRealmModel? = null
            realm?.executeTransaction {
                item = it.where(PartnerRealmModel::class.java)?.equalTo(PARTNER_COL_ZONE, zone)?.equalTo(PARTNER_COL_BRANCH, branch)?.equalTo(PARTNER_COL_PARTNER, pratner)?.findFirst()
            }
            return item?.email
        } finally {
            realm?.close()
        }
    }
}