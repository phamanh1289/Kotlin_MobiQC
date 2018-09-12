package vn.com.fpt.mobiqc.data.realm.infrastructure

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * * Created by Anh Pham on 08/17/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
open class InfrastructureRealmModel(@PrimaryKey var id: Int = 0, var type: String = "", var createdate: String = "", var description: String = "") : RealmObject()
