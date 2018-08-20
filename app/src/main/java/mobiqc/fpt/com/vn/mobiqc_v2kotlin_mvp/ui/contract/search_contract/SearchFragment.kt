package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.search_contract

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_search.*
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.R
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.SingleChoiceModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.network.model.TitleAndMenuModel
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.data.realm.location.LocationRealmManager
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.constant.Constants
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.others.datacore.DataCore
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.search.SearchCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.AppUtils
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.utils.KeyboardUtils

/**
 * * Created by Anh Pham on 08/09/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class SearchFragment : BaseFragment() {

    private var listParent = ArrayList<SingleChoiceModel>()
    private var listName = ArrayList<SingleChoiceModel>()
    private var listTypeSearch = ArrayList<SingleChoiceModel>()
    private var positionParent = 0
    private var positionName = 0
    private var positionTypeSearch = 0
    private var typeMenu = 0
    private var titleFragment = ""

    companion object {
        const val TYPE_SEARCH_MAC = 4

        fun newInstance(type: Int, title: String): SearchFragment {
            val args = Bundle()
            args.putInt(Constants.ARG_TYPE_MENU, type)
            args.putString(Constants.ARG_TITLE, title)
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
        activity?.let { KeyboardUtils.setupUI(view, activity = it) }
        initView()
    }

    private fun initView() {
        handleArgument()
        getAllDataList()
        initOnClick()
    }

    private fun handleArgument() {
        arguments?.let {
            typeMenu = it.getInt(Constants.ARG_TYPE_MENU)
            titleFragment = it.getString(Constants.ARG_TITLE) ?: ""
        }
        setTitle(TitleAndMenuModel(title = titleFragment, status = true, image = R.drawable.ic_notifications))
    }

    private fun initOnClick() {
        fragSearch_etKeyWord.onChange { it }
        fragSearch_imgClearText.setOnClickListener { fragSearch_etKeyWord.setText("") }
        fragSearch_tvLocation.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_location), listParent, fragSearch_tvLocation, positionParent) }
        fragSearch_tvBranch.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_branch), listName, fragSearch_tvBranch, positionName) }
        fragSearch_tvTypeSearch.setOnClickListener { AppUtils.showDialogSingChoice(fragmentManager, getString(R.string.search_title), listTypeSearch, fragSearch_tvTypeSearch, positionTypeSearch) }
        fragSearch_tvSubmit.setOnClickListener { handleActionSubmit() }
    }

    private fun getAllDataList() {
        listParent = LocationRealmManager().getDistinctParent()
        getDataNameDesc(positionParent)
        listTypeSearch = DataCore.getListSearch(context)
        setDefaultFirstValue()
    }

    private fun getDataNameDesc(position: Int) {
        listName = LocationRealmManager().getDistinctNameDesc(listParent[position].account)
        listName[Constants.FIRST_ITEM].let { fragSearch_tvBranch.text = it.account }
    }

    fun setDefaultValueIndex(view: Int, index: Int) {
        when (view) {
            R.id.fragSearch_tvLocation -> {
                positionParent = index
                getDataNameDesc(positionParent)
                positionName = Constants.FIRST_ITEM
            }
            R.id.fragSearch_tvBranch -> {
                positionName = index
            }
            R.id.fragSearch_tvTypeSearch -> {
                positionTypeSearch = index
            }
        }
    }

    private fun setDefaultFirstValue() {
        listParent[Constants.FIRST_ITEM].let {
            fragSearch_tvLocation.text = it.account
        }
        listTypeSearch[Constants.FIRST_ITEM].let {
            fragSearch_tvTypeSearch.text = it.account
        }
    }

    private fun handleActionSubmit() {
        val keySearch = if (listTypeSearch[positionTypeSearch].id == TYPE_SEARCH_MAC) AppUtils.convertStringToMacAddress(fragSearch_etKeyWord.text.toString()) else fragSearch_etKeyWord.text.toString()
        if (keySearch.isBlank())
            AppUtils.showDialog(fragmentManager, content = getString(R.string.validate_key_search), confirmDialogInterface = null)
        else {
            addFragment(SearchCheckListFragment.newInstance(typeMenu, listTypeSearch[positionTypeSearch].id, keySearch, listParent[positionParent].id, false), true, true)
        }
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fragSearch_imgClearText.visibility = if (s?.length != 0) View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}