package vn.com.fpt.mobiqc.data.realm.partner

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * * Created by Anh Pham on 08/17/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class PartnerRealmModel(@PrimaryKey var id: Int = 0, var branch: String = "", var createdate: String = "", var email: String = "", var partner: String = "", var zone: String = "") : RealmObject()
