package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.search_contract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_search.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.LocationUserModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank.SearchContract
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.blank.SearchPresenter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils
import javax.inject.Inject

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SearchFragment : BaseFragment(), SearchContract.SearchView {
    @Inject
    lateinit var presenter: SearchPresenter

    private lateinit var listLocationUser: ArrayList<LocationUserModel>
    private var listLocation = ArrayList<SingleChoiceModel>()
    private var listBranch = ArrayList<SingleChoiceModel>()
    private var listTypeSearch = ArrayList<SingleChoiceModel>()

    companion object {
        fun newInstance(type: Int): SearchFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_CONTRACT, type)
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getActivityComponent().inject(this)
        presenter.onAttach(this)
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        getAllDataList()
        setDefaultFirstValue()
        initOnClick()
    }

    private fun initOnClick() {
        fragSearch_tvLocation.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_location), listLocation, fragSearch_tvLocation) }
        fragSearch_tvBranch.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_branch), listBranch, fragSearch_tvBranch) }
        fragSearch_tvTypeSearch.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_title), listTypeSearch, fragSearch_tvTypeSearch) }
        fragSearch_tvSubmit.setOnClickListener { handleActionSubmit() }
    }

    private fun getAllDataList() {
        listLocationUser = Gson().fromJson(getSharePreferences().listLocationUser, object : TypeToken<ArrayList<LocationUserModel>>() {}.type)
        for (i in 0 until listLocationUser.size) {
            val model = listLocationUser[i]
            listLocation.add(SingleChoiceModel(account = model.parentdesc, status = i == Constants.FIRST_ITEM))
            listBranch.add(SingleChoiceModel(account = model.namedesc, status = i == Constants.FIRST_ITEM))
        }
        listTypeSearch = DataCore.getListSearch(context)
    }

    private fun setDefaultFirstValue() {
        listLocation[Constants.FIRST_ITEM].let {
            fragSearch_tvLocation.text = it.account
        }
        listBranch[Constants.FIRST_ITEM].let {
            fragSearch_tvBranch.text = it.account
        }
        listTypeSearch[Constants.FIRST_ITEM].let {
            fragSearch_tvTypeSearch.text = it.account
        }
    }

    private fun handleActionSubmit() {
        val keySearch = fragSearch_etKeyWord.text
        if (keySearch.isBlank())
            AppUtils.showDialog(fragmentManager, content = getString(R.string.validate_key_search), confirmDialogInterface = null)
        else {
        }
    }

    override fun handleError(error: String) {
        hideLoading()
        AppUtils.showDialog(fragmentManager, content = error, confirmDialogInterface = null)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}