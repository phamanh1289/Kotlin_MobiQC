package vn.com.fpt.mobiqc.data.realm.error

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * * Created by Anh Pham on 08/06/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class ErrorRealmModel(@PrimaryKey var id: Int = 0, var department: String = "", var type: String = "", var main: String = "", var description: String = "", var createdate: String = "") : RealmObject()