package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.base.BaseFragment
import java.util.*

/**
 * * Created by Anh Pham on 08/13/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
class BaseViewPagerAdapter(fragmentManager: FragmentManager?) : FragmentStatePagerAdapter(fragmentManager) {
    private val MAX_TAB = 2

    private val mFragmentList = ArrayList<BaseFragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): BaseFragment {
        return mFragmentList[position]
    }

    fun addTabFragment(fragment: BaseFragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
    override fun getCount(): Int {
        return MAX_TAB
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}
