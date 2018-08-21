package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component

import dagger.Subcomponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module.ActivityModule
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.scope.ActivityScope
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_check_list.CreateCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.create_pre_check_list.CreatePreCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.search.SearchCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.all_check_list.AllCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.deployment_check_list.DeploymentCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.check_list.maintenance_check_list.MaintenanceCheckListFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.DetailContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.dialog_detail_contract.ContractDetailDialog
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.InfoContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.error.ErrorFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.login.LoginFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.main.MainActivity
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.splash_screen.SplashScreenActivity

/**
 * * Created by Anh Pham on 08/02/2018.     **
 * * Copyright (c) 2018 by FPT Telecom      **
 */
@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(infoContractFragment: InfoContractFragment)
    fun inject(detailContractFragment: DetailContractFragment)
    fun inject(deploymentCheckListFragment: DeploymentCheckListFragment)
    fun inject(maintenanceCheckListFragment: MaintenanceCheckListFragment)
    fun inject(errorFragment: ErrorFragment)
    fun inject(allCheckListFragment: AllCheckListFragment)
    fun inject(contractDetailDialog: ContractDetailDialog)
    fun inject(searchCheckListFragment: SearchCheckListFragment)
    fun inject(createCheckListFragment: CreateCheckListFragment)
    fun inject(createPreCheckListFragment: CreatePreCheckListFragment)
}