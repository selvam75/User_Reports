package com.userreports.data

import android.app.Activity
import com.userreports.UserReportApplication.Companion.instances

object UserPreferences {

  private const val PrefName = "reportPreference"
  var PAGE_LIMIT = "page_limit"

  fun saveInt(name: String?, value: Int) {
    val pref = instances!!.getSharedPreferences(PrefName, Activity.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putInt(name, value)
    editor.apply()
  }

  fun getSavedInt(name: String?): Int {
    val pref = instances!!.getSharedPreferences(PrefName, Activity.MODE_PRIVATE)
    return pref.getInt(name, 0)
  }
}