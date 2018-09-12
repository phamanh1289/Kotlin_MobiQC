package vn.com.fpt.mobiqc.dagger.component

import com.google.android.gms.maps.MapFragment
import dagger.Subcomponent
import vn.com.fpt.mobiqc.dagger.module.ActivityModule
import vn.com.fpt.mobiqc.dagger.scope.ActivityScope
import vn.com.fpt.mobiqc.ui.check_list.create_check_list.CreateCheckListFragment
import vn.com.fpt.mobiqc.ui.check_list.create_pre_check_list.CreatePreCheckListFragment
import vn.com.fpt.mobiqc.ui.check_list.deployment_check_list.DeploymentCheckListFragment
import vn.com.fpt.mobiqc.ui.check_list.maintenance_check_list.MaintenanceCheckListFragment
import vn.com.fpt.mobiqc.ui.check_list.search.SearchCheckListFragment
import vn.com.fpt.mobiqc.ui.contract.detail_contract.DetailContractFragment
import vn.com.fpt.mobiqc.ui.contract.dialog_detail_contract.DetailContractDialogFragment
import vn.com.fpt.mobiqc.ui.contract.info_contract.InfoContractFragment
import vn.com.fpt.mobiqc.ui.error.create.CreateErrorFragment
import vn.com.fpt.mobiqc.ui.error.list.ListErrorFragment
import vn.com.fpt.mobiqc.ui.error.update.UpdateErrorFragment
import vn.com.fpt.mobiqc.ui.image.upload_image.UploadImageFragment
import vn.com.fpt.mobiqc.ui.image.view_image.ViewImageFragment
import vn.com.fpt.mobiqc.ui.login.LoginFragment
import vn.com.fpt.mobiqc.ui.main.MainActivity
import vn.com.fpt.mobiqc.ui.maps.MapsFragment
import vn.com.fpt.mobiqc.ui.reprot.ReportFragment
import vn.com.fpt.mobiqc.ui.splash_screen.SplashScreenActivity

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
    fun inject(updateErrorFragment: UpdateErrorFragment)
    fun inject(detailContractDialogFragment: DetailContractDialogFragment)
    fun inject(searchCheckListFragment: SearchCheckListFragment)
    fun inject(createCheckListFragment: CreateCheckListFragment)
    fun inject(createPreCheckListFragment: CreatePreCheckListFragment)
    fun inject(createErrorFragment: CreateErrorFragment)
    fun inject(listErrorFragment: ListErrorFragment)
    fun inject(uploadImageFragment: UploadImageFragment)
    fun inject(viewImageFragment: ViewImageFragment)
    fun inject(reportFragment: ReportFragment)
    fun inject(mapFragment: MapsFragment)
}