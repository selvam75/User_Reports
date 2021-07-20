package com.userreports.base

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.userreports.R
import com.userreports.UserReportApplication

abstract class BaseActivity : AppCompatActivity() {

  fun loadFragment(fragment: Fragment?) {
    if (fragment != null) {
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.frm_content_frame, fragment)
        .commit()
    }
  }

  @Suppress("DEPRECATION")
  fun checkNetwork(): Boolean {
    var result = false
    val connectivityManager =
      UserReportApplication.instances!!.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val networkCapabilities = connectivityManager.activeNetwork ?: return false
      val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
      result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
      }
    } else {
      connectivityManager.run {
        connectivityManager.activeNetworkInfo?.run {
          result = when (type) {
            ConnectivityManager.TYPE_WIFI -> true
            ConnectivityManager.TYPE_MOBILE -> true
            ConnectivityManager.TYPE_ETHERNET -> true
            else -> false
          }

        }
      }
    }

    return result
  }

  companion object {
    var mActivity: AppCompatActivity? = null
  }

  fun isNetworkAvailable(): Boolean {
    return checkNetwork()
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStack()
    } else
      finish()
  }

}