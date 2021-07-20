package com.userreports.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.userreports.R
import com.userreports.base.BaseActivity.Companion.mActivity


abstract class BaseFragment : Fragment() {
  private var mConnectivityManager: ConnectivityManager? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mConnectivityManager =
      mActivity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
  }

  fun loadFragment(fragment: Fragment, bundle: Bundle) {
    loadFragment(fragment, true, bundle, true)
  }

  internal fun loadFragment(
    fragment: Fragment,
    addBackStack: Boolean,
    bundle: Bundle?,
    canAnimate: Boolean
  ): Fragment {
    val fragmentManager = mActivity!!.supportFragmentManager
    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
    if (bundle != null) {
      fragment.arguments = bundle
    }
    if (canAnimate) {
      transaction.setCustomAnimations(
        R.anim.enter_from_right,
        R.anim.exit_to_left,
        R.anim.enter_from_left,
        R.anim.exit_to_right
      )
    }
    if (addBackStack) {
      transaction.replace(R.id.frm_content_frame, fragment).addToBackStack(fragment.javaClass.name)
        .commit()
    } else
      transaction.replace(R.id.frm_content_frame, fragment).commitAllowingStateLoss()
    return fragment
  }

  @Suppress("DEPRECATION")
  fun checkNetwork(): Boolean {
    var result = false
    val connectivityManager =
      mActivity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

  open fun isNetworkAvailable(): Boolean {
    return checkNetwork()
  }

}