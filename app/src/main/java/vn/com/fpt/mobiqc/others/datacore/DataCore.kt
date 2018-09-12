package vn.com.fpt.mobiqc.others.datacore

import android.content.Context
import android.support.v4.content.ContextCompat
import com.github.mikephil.charting.utils.ColorTemplate
import vn.com.fpt.mobiqc.R
import vn.com.fpt.mobiqc.data.network.model.ErrorInfrastructureModel
import vn.com.fpt.mobiqc.data.network.model.ItemMenuModel
import vn.com.fpt.mobiqc.data.network.model.SingleChoiceModel
import vn.com.fpt.mobiqc.others.constant.Constants

/**
 * * Created by Anh Pham on 08/07/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
object DataCore {
    fun getListMenu(context: Context): ArrayList<ItemMenuModel> {
        val list = ArrayList<ItemMenuModel>()
        context.let {
            list.add(ItemMenuModel(name = it.getString(R.string.menu_ks_dich_vu)))
            list.add(ItemMenuModel(id = Constants.KT_HOP_DONG, name = it.getString(R.string.menu_kt_hop_dong)))
            list.add(ItemMenuModel(id = Constants.CAP_NHAT_LOI, name = it.getString(R.string.menu_cap_nhat_loi)))
            list.add(ItemMenuModel(id = Constants.TAO_CHECK_LIST, name = it.getString(R.string.menu_tao_check_list)))
            list.add(ItemMenuModel(id = Constants.TAO_PRE_CHECK_LIST, name = it.getString(R.string.menu_tao_pre_check_list)))
            list.add(ItemMenuModel(name = it.getString(R.string.menu_ks_ha_tang)))
            list.add(ItemMenuModel(id = Constants.TAO_LOI_MOI, name = it.getString(R.string.menu_tao_loi_moi)))
            list.add(ItemMenuModel(id = Constants.DANH_SACH_LOI, name = it.getString(R.string.menu_danh_sach_loi)))
            list.add(ItemMenuModel(name = it.getString(R.string.menu_khac)))
            list.add(ItemMenuModel(id = Constants.KQ_XAC_MINH, name = it.getString(R.string.menu_kq_xac_minh)))
            list.add(ItemMenuModel(id = Constants.BAO_CAO_SO_LIEU, name = it.getString(R.string.menu_bao_cao_so_lieu)))
            list.add(ItemMenuModel(id = Constants.THONG_TIN, name = it.getString(R.string.menu_thong_tin)))
            list.add(ItemMenuModel(id = Constants.DANG_XUAT, name = it.getString(R.string.menu_dang_xuat)))
        }
        return list
    }

    fun getListLoaiTC(context: Context): ArrayList<SingleChoiceModel> {
        val list = ArrayList<SingleChoiceModel>()
        context.let {
            list.add(SingleChoiceModel(id = 1, account = it.getString(R.string.loai_tc_1)))
            list.add(SingleChoiceModel(id = 2, account = it.getString(R.string.loai_tc_2)))
        }
        return list
    }

    fun getListTypeKS(context: Context?): ArrayList<SingleChoiceModel> {
        val list = ArrayList<SingleChoiceModel>()
        context?.let {
            list.add(SingleChoiceModel(id = Constants.TYPE_KS_HOT, account = it.getString(R.string.type_KS_hot), status = true))
            list.add(SingleChoiceModel(id = Constants.TYPE_KS_COOL, account = it.getString(R.string.type_KS_cold)))
            list.add(SingleChoiceModel(id = Constants.TYPE_KS_SUBJ, account = it.getString(R.string.type_KS_subject)))
            list.add(SingleChoiceModel(id = Constants.TYPE_KS_SWAP, account = it.getString(R.string.type_KS_swap)))
        }
        return list
    }

    fun getListIndoor(context: Context?): ArrayList<SingleChoiceModel> {
        val list = ArrayList<SingleChoiceModel>()
        context?.let {
            list.add(SingleChoiceModel(id = 0, account = it.getString(R.string.type_indoor_no), status = true))
            list.add(SingleChoiceModel(id = 1, account = it.getString(R.string.type_indoor_yes)))
        }
        return list
    }

    fun getListSearch(context: Context?): ArrayList<SingleChoiceModel> {
        val list = ArrayList<SingleChoiceModel>()
        context?.let {
            list.add(SingleChoiceModel(id = 1, account = it.getString(R.string.data_search_contract_number), status = true))
            list.add(SingleChoiceModel(id = 2, account = it.getString(R.string.data_search_user_name)))
            list.add(SingleChoiceModel(id = 3, account = it.getString(R.string.data_search_phone)))
            list.add(SingleChoiceModel(id = 4, account = it.getString(R.string.data_search_address_mac)))
            list.add(SingleChoiceModel(id = 5, account = it.getString(R.string.data_search_id)))
        }
        return list
    }

    fun getListFirstStatus(context: Context?): ArrayList<SingleChoiceModel> {
        val list = ArrayList<SingleChoiceModel>()
        context?.let {
            list.add(SingleChoiceModel(id = 4, account = it.getString(R.string.first_status_1), status = true))
            list.add(SingleChoiceModel(id = 23, account = it.getString(R.string.first_status_2)))
            list.add(SingleChoiceModel(id = 50, account = it.getString(R.string.first_status_3)))
            list.add(SingleChoiceModel(id = 46, account = it.getString(R.string.first_status_4)))
            list.add(SingleChoiceModel(id = 47, account = it.getString(R.string.first_status_5)))
            list.add(SingleChoiceModel(id = 45, account = it.getString(R.string.first_status_6)))
            list.add(SingleChoiceModel(id = 49, account = it.getString(R.string.first_status_7)))
            list.add(SingleChoiceModel(id = 53, account = it.getString(R.string.first_status_8)))
            list.add(SingleChoiceModel(id = 8, account = it.getString(R.string.first_status_9)))
            list.add(SingleChoiceModel(id = 9, account = it.getString(R.string.first_status_10)))
            list.add(SingleChoiceModel(id = 51, account = it.getString(R.string.first_status_11)))
            list.add(SingleChoiceModel(id = 52, account = it.getString(R.string.first_status_12)))
            list.add(SingleChoiceModel(id = 12, account = it.getString(R.string.first_status_13)))
            list.add(SingleChoiceModel(id = 21, account = it.getString(R.string.first_status_14)))
            list.add(SingleChoiceModel(id = 48, account = it.getString(R.string.first_status_15)))
            list.add(SingleChoiceModel(id = 5, account = it.getString(R.string.first_status_16)))
            list.add(SingleChoiceModel(id = 55, account = it.getString(R.string.first_status_17)))
            list.add(SingleChoiceModel(id = 58, account = it.getString(R.string.first_status_18)))
            list.add(SingleChoiceModel(id = 103, account = it.getString(R.string.first_status_19)))
            list.add(SingleChoiceModel(id = 104, account = it.getString(R.string.first_status_20)))
            list.add(SingleChoiceModel(id = 18, account = it.getString(R.string.first_status_21)))
            list.add(SingleChoiceModel(id = 38, account = it.getString(R.string.first_status_22)))
            list.add(SingleChoiceModel(id = 33, account = it.getString(R.string.first_status_23)))
            list.add(SingleChoiceModel(id = 40, account = it.getString(R.string.first_status_24)))
            list.add(SingleChoiceModel(id = 29, account = it.getString(R.string.first_status_25)))
            list.add(SingleChoiceModel(id = 31, account = it.getString(R.string.first_status_26)))
            list.add(SingleChoiceModel(id = 36, account = it.getString(R.string.first_status_27)))
            list.add(SingleChoiceModel(id = 37, account = it.getString(R.string.first_status_28)))
            list.add(SingleChoiceModel(id = 68, account = it.getString(R.string.first_status_29)))
            list.add(SingleChoiceModel(id = 69, account = it.getString(R.string.first_status_30)))
            list.add(SingleChoiceModel(id = 70, account = it.getString(R.string.first_status_31)))
            list.add(SingleChoiceModel(id = 71, account = it.getString(R.string.first_status_32)))
            list.add(SingleChoiceModel(id = 0, account = it.getString(R.string.first_status_33)))
        }
        return list
    }

    fun getPreFirstStatus(context: Context?): ArrayList<SingleChoiceModel> {
        val list = ArrayList<SingleChoiceModel>()
        context?.let {
            list.add(SingleChoiceModel(id = 5, account = it.getString(R.string.pre_first_status_1), status = true))
            list.add(SingleChoiceModel(id = 1, account = it.getString(R.string.pre_first_status_2)))
            list.add(SingleChoiceModel(id = 6, account = it.getString(R.string.pre_first_status_3)))
            list.add(SingleChoiceModel(id = 7, account = it.getString(R.string.pre_first_status_4)))
            list.add(SingleChoiceModel(id = 2, account = it.getString(R.string.pre_first_status_5)))
            list.add(SingleChoiceModel(id = 3, account = it.getString(R.string.pre_first_status_6)))
            list.add(SingleChoiceModel(id = 4, account = it.getString(R.string.pre_first_status_7)))
        }
        return list
    }

    fun getBaseBodyMail(error: ErrorInfrastructureModel, urlImage: String): String {
        return "<html><head><meta http-equiv=Content-Type content=\"text/html; charset=utf-8\">\n" +
                "</head><body lang=VI link=blue vlink=purple><div class=WordSection1>" +
                "<p ><span>Dear Anh/Chị,<o:p></o:p></span></p>" +
                "<p ><span>FTQ kiểm tra hạ tầng ghi nhận lỗi sau. </br>Kính nhờ Anh/Chị nhanh chóng khắc phục lỗi và phản hồi thông tin xử lý lại cho FTQ trong vòng 1 tuần kể từ khi FTQ gởi thông báo." +
                "</br>Thông tin phản hồi có gửi kèm hình ảnh kết quả đã xử lý.<o:p></o:p></span></p>" +
                "<ul><li>ID: <b> ${error.ID} </b></li>" +
                "<li>Vùng: <b> ${error.Area} </b></li>" +
                "<li>Chi nhánh: <b> ${error.Branch} </b></li>" +
                "<li>Lỗi: <b> ${error.Type} </b></li>" +
                "<li>Phần tử lỗi: <b> ${error.Element} </b></li>" +
                "<li>Mô tả lỗi: <b> ${error.Description} </b></li>" +
                "<li>Ghi chú: <b> ${error.Note} </b></li>" +
                "<li>Nhân sự ghi nhận lỗi: <b> ${error.CreateBy} </b></li>" +
                "<li>Thời gian ghi nhận lỗi: <b> ${error.CreateDate} </b></li></ul>" +
                "<p ><span>Trân trọng cảm ơn!<o:p></o:p></span></p>" +
                "<p > $urlImage </p><h4><span><o:p>&nbsp;</o:p></span></h4></div></body></html>"
    }

    fun getListcolor(context: Context?): ArrayList<Int> {
        val colors = ArrayList<Int>()
        context?.let {
            colors.add(ContextCompat.getColor(it, R.color.bright_blue))
            colors.add(ContextCompat.getColor(it, R.color.bright_red))
            colors.add(ContextCompat.getColor(it, R.color.blue))
            colors.add(ContextCompat.getColor(it, R.color.red_text))
            colors.add(ContextCompat.getColor(it, R.color.colorPrimary))
            colors.add(ContextCompat.getColor(it, R.color.steel))
            colors.add(ColorTemplate.getHoloBlue())
        }
        return colors
    }
}