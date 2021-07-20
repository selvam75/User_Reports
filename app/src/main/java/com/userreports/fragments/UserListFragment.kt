package com.userreports.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.userreports.R
import com.userreports.UserReportApplication
import com.userreports.activities.HomeActivity
import com.userreports.adapter.UserListAdapter
import com.userreports.base.AdapterCallback
import com.userreports.base.AppConstants
import com.userreports.base.BaseFragment
import com.userreports.base.Resource
import com.userreports.data.AppDatabase
import com.userreports.data.UserPreferences
import com.userreports.data.WeatherDataEntity
import com.userreports.data.userlistdao.UserListModel
import com.userreports.databinding.FragmentUserListBinding
import com.userreports.viewModel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class UserListFragment : BaseFragment() {
  private var mLoading = true
  private lateinit var mUserListViewModel: ReportViewModel
  private var mHomeView: FragmentUserListBinding? = null
  private var mUserListAdapter: UserListAdapter? = null
  private lateinit var mLayoutManager: LinearLayoutManager
  private var mLimit = 25
  private val mMinimumTime: Long = 5000
  private val mMinimumDistance = 1000f
  private var mLocationProvider = LocationManager.GPS_PROVIDER
  private var mLocationManager: LocationManager? = null
  private var mLocationListener: LocationListener? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    mUserListViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
    if (mHomeView == null) {
      mHomeView = FragmentUserListBinding.inflate(inflater, container, false)
      initViews()
    }
    return mHomeView!!.root
  }

  private fun initViews() {
    mLayoutManager = LinearLayoutManager(activity)
    mHomeView!!.rvUserList.layoutManager = mLayoutManager
    mHomeView!!.rvUserList.isNestedScrollingEnabled = false
    mHomeView!!.nsUserReport.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->

      if (scrollY == (v!!.getChildAt(0).measuredHeight - v.measuredHeight)) {
        if (mLoading && isNetworkAvailable()) {
          mLoading = mUserListViewModel.mCanPaginate
          mLimit += UserPreferences.getSavedInt(UserPreferences.PAGE_LIMIT)
          mUserListViewModel.getUserLists(mLimit).observe(viewLifecycleOwner, { list ->
            if (list.status == Resource.Status.SUCCESS) {
              UserPreferences.saveInt(UserPreferences.PAGE_LIMIT, mLimit)
              if (mUserListAdapter != null)
                mUserListAdapter!!.updateUserList(list.data!! as ArrayList<UserListModel>)
            }
          })
        }
      }
    })
    mHomeView!!.llRefreshWeather.setOnClickListener {
      getWeatherForCurrentLocation()
    }

    getWeatherForCurrentLocation()
    setAdapter()
    if (AppDatabase.getInstance(UserReportApplication.instances!!)!!.locationDao()!!
        .locationData != null
    ) {

      mHomeView!!.tvUpdatedDate.text = getString(R.string.fetching)
    }

  }

  private fun setAdapter() {
    val usersListDao = AppDatabase.getInstance(UserReportApplication.instances!!)!!.userListDao()!!
    val userListUpdateObserver =
      Observer { usersList: MutableList<UserListModel> ->
        if (usersList.size > 0) {
          mUserListAdapter =
            UserListAdapter(usersList as ArrayList<UserListModel>, object : AdapterCallback {
              override fun onItemClicked(position: Int) {
                val bundle = Bundle()
                bundle.putString(AppConstants.SELECTED_USER_ID, usersList[position].id)
                loadFragment(UserDetailFragment(), bundle)
              }
            })
          mHomeView!!.rvUserList.adapter = mUserListAdapter
        }
      }
    usersListDao.allUsersList.observe(requireActivity(), userListUpdateObserver)
  }

  fun filterUser(searchText: String) {
    mUserListAdapter!!.filter.filter(searchText)
  }

  private fun getWeatherForCurrentLocation() {
    mHomeView!!.tvUpdatedDate.text = getString(R.string.fetching)
    mLocationManager =
      requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    mLocationListener = object : LocationListener {
      override fun onLocationChanged(location: Location) {
        fetchWeatherReport(
          location.latitude.toString(),
          location.longitude.toString()
        )
      }

      override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
      override fun onProviderEnabled(provider: String) {}
      override fun onProviderDisabled(provider: String) {}
    }
    if (requireContext().let {
        ContextCompat.checkSelfPermission(
          it,
          Manifest.permission.ACCESS_FINE_LOCATION
        )
      } != PackageManager.PERMISSION_GRANTED) {

      requestPermission.launch(
        Manifest.permission.ACCESS_FINE_LOCATION
      )
      return
    }

    mLocationManager!!.requestLocationUpdates(
      mLocationProvider,
      mMinimumTime,
      mMinimumDistance,
      mLocationListener!!
    )
  }

  private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    if (it) {
      getWeatherForCurrentLocation()
    } else {
      if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        }
      } else {
        val alertBuilder = AlertDialog.Builder(requireActivity())
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(R.string.permission_required)
        alertBuilder.setMessage(R.string.permission_location_message)
        alertBuilder.setPositiveButton(
          android.R.string.ok
        ) { alert, _ ->
          alert.dismiss()
        }
        val alert = alertBuilder.create()
        alert.setCancelable(false)
        alert.setCanceledOnTouchOutside(false)
        alert.show()
      }
    }
  }

  @SuppressLint("SimpleDateFormat")
  private fun fetchWeatherReport(latitude: String, longitude: String) {
    if (isNetworkAvailable()) {
      mUserListViewModel.fetchWeatherReport(latitude, longitude).observe(viewLifecycleOwner, {
        if (it.status == Resource.Status.SUCCESS) {
          val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm aa")
          val formattedDate: String =
            getString(R.string.updated_at) + dateFormat.format(Date()).toString()
          mHomeView!!.tvUpdatedDate.text = formattedDate
          mUserListViewModel.saveLocationData(latitude, longitude, formattedDate)
          val weatherData = it.data as WeatherDataEntity
          updateWeatherUI(weatherData)
        }
      })
    }
  }

  private fun updateWeatherUI(weatherData: WeatherDataEntity) {
    val tempResult: Double = weatherData.main!!.temp - 273.15
    val roundedValue = round(tempResult).toInt().toString() + AppConstants.KEY_DEGREE
    mHomeView!!.tvTemperature.text = roundedValue
    mHomeView!!.tvCity.text = weatherData.name
    mHomeView!!.tvWeatherState.text = weatherData.weather?.get(0)!!.main

    val resourceID = resources.getIdentifier(
      mUserListViewModel.updateWeatherIcon(weatherData.weather?.get(0)!!.id),
      AppConstants.DRAWABLE,
      requireActivity().packageName
    )
    mHomeView!!.ivWeatherIcon.setImageResource(resourceID)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
  ) {
    if (requestCode == AppConstants.LOCATION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getWeatherForCurrentLocation()
      } else {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
          )
        ) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
          }
        } else {
          if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            val alertBuilder = AlertDialog.Builder(requireActivity())
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle(R.string.permission_required)
            alertBuilder.setMessage(R.string.permission_location_message)
            alertBuilder.setPositiveButton(
              android.R.string.ok
            ) { alert, _ ->
              alert.dismiss()
            }
            val alert = alertBuilder.create()
            alert.setCancelable(false)
            alert.setCanceledOnTouchOutside(false)
            alert.show()
            return
          }
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    (activity as HomeActivity).showSearchMenu(true, getString(R.string.app_name))
  }

  override fun onDestroy() {
    super.onDestroy()
    if (mLocationManager != null) {
      mLocationManager!!.removeUpdates(mLocationListener!!)
    }
  }

}