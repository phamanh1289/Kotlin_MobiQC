package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore

import android.content.Context
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.ItemMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants

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
            list.add(SingleChoiceModel(id = 6, account = it.getString(R.string.type_KS_hot), status = true))
            list.add(SingleChoiceModel(id = 7, account = it.getString(R.string.type_KS_cold)))
            list.add(SingleChoiceModel(id = 4, account = it.getString(R.string.type_KS_subject)))
            list.add(SingleChoiceModel(id = 5, account = it.getString(R.string.type_KS_swap)))
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
            list.add(SingleChoiceModel(id = 0, account = it.getString(R.string.data_search_contract_number), status = true))
            list.add(SingleChoiceModel(id = 1, account = it.getString(R.string.data_search_user_name)))
            list.add(SingleChoiceModel(id = 2, account = it.getString(R.string.data_search_phone)))
            list.add(SingleChoiceModel(id = 3, account = it.getString(R.string.data_search_address_mac)))
            list.add(SingleChoiceModel(id = 4, account = it.getString(R.string.data_search_id)))
        }
        return list
    }
}