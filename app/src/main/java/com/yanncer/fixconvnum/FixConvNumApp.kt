package com.yanncer.fixconvnum

import android.app.Application
import com.yanncer.fixconvnum.common.PrefSingleton
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FixConvNumApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PrefSingleton.init(this)
    }
}