package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model


/**
 * * Created by Anh Pham on 08/30/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class PolylineMapModel(val routes: ArrayList<RouteModel>) : BaseModel() {

    inner class RouteModel(val legs: ArrayList<LegModel>) : BaseModel()

    inner class LegModel(val steps: ArrayList<StepModel>) : BaseModel()

    inner class StepModel(val end_location: EndLocationMdeol, val start_location: EndLocationMdeol) : BaseModel()

    inner class EndLocationMdeol(val lat: Double, val lng: Double) : BaseModel()
}
