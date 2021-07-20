package com.userreports

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.androidnetworking.AndroidNetworking

class UserReportApplication : Application() {
  companion object {
    var instances: UserReportApplication? = null
  }

  override fun onCreate() {
    super.onCreate()
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    instances = this
    AndroidNetworking.initialize(this)
  }

}