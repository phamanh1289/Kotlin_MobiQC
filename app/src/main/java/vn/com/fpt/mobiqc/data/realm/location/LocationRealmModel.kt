package vn.com.fpt.mobiqc.data.realm.location

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * * Created by Anh Pham on 08/17/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class LocationRealmModel(@PrimaryKey var id: Int = 0, var namedesc: String = "", var parentdesc: String = "") : RealmObject()