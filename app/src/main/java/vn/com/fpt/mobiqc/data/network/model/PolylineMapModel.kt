package vn.com.fpt.mobiqc.data.network.model


/**
 * * Created by Anh Pham on 08/30/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class PolylineMapModel(val routes: ArrayList<RouteModel>) : BaseModel() {
    inner class RouteModel( val overview_polyline: OverviewPolylineModel) : BaseModel()
    inner class OverviewPolylineModel(val points: String) : BaseModel()
}
