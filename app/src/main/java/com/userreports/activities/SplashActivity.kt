package com.userreports.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.userreports.R
import com.userreports.UserReportApplication
import com.userreports.base.BaseActivity
import com.userreports.base.Resource
import com.userreports.data.AppDatabase
import com.userreports.data.UserPreferences
import com.userreports.data.userlistdao.UserListModel
import com.userreports.databinding.ActivitySplashBinding
import com.userreports.viewModel.ReportViewModel

class SplashActivity : BaseActivity() {

  private lateinit var mSplashBinding: ActivitySplashBinding
  private lateinit var mReportViewModel: ReportViewModel
  private val mLimit = 25

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mReportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
    mSplashBinding = ActivitySplashBinding.inflate(layoutInflater)
    setContentView(mSplashBinding.root)
    navigationFlow()
  }

  companion object {
    private const val TIME_OUT: Long = 1000L
  }

  private fun navigationFlow() {
    val usersListDao = AppDatabase.getInstance(UserReportApplication.instances!!)!!.userListDao()!!
    val userListUpdateObserver =
      Observer { usersList: MutableList<UserListModel> ->
        if (usersList.size > 0) {
          navigateToHome()
        } else {
          checkNetworkFlow()
        }
      }
    usersListDao.allUsersList.observe(this@SplashActivity, userListUpdateObserver)
  }

  private fun checkNetworkFlow() {
    if (isNetworkAvailable()) {
      mReportViewModel.getUserLists(mLimit).observe(this, {
        if (it.status == Resource.Status.SUCCESS) {
          UserPreferences.saveInt(UserPreferences.PAGE_LIMIT, mLimit)
          AppDatabase.getInstance(this)!!.userListDao()!!.insertUsersList(it.data!!)
          navigateToHome()
        }
      })
    } else {
      Snackbar
        .make(mSplashBinding.root, getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(R.string.retry)) { navigationFlow() }
        .setActionTextColor(
          ContextCompat.getColor(
            this@SplashActivity,
            R.color.white
          )
        )
        .show()
    }
  }

  private fun navigateToHome() {
    mSplashBinding.root.postDelayed({
      startActivity(
        Intent(this@SplashActivity, HomeActivity::class.java)
      )
      finish()
    }, TIME_OUT)
  }

}