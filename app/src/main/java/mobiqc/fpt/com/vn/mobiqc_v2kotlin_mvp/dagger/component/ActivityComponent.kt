package mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.component

import dagger.Subcomponent
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.module.ActivityModule
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.dagger.scope.ActivityScope
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.check_contract.CheckContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.detail_contract.DetailContractFragment
import mobiqc.fpt.com.vn.mobiqc_v2kotlin_mvp.ui.contract.info_contract.InfoContractFragment
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
    fun inject(checkContractFragment: CheckContractFragment)
    fun inject(infoContractFragment: InfoContractFragment)
    fun inject(detailContractFragment: DetailContractFragment)
}