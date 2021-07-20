package com.userreports.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.userreports.R
import com.userreports.activities.HomeActivity
import com.userreports.base.AppConstants
import com.userreports.base.BaseFragment
import com.userreports.base.Resource
import com.userreports.data.UserDetailsModel
import com.userreports.data.WeatherDataEntity
import com.userreports.data.userlistdao.UserListModel
import com.userreports.databinding.FragmentUserDetailBinding
import com.userreports.databinding.InflateUserReportDetailItemBinding
import com.userreports.viewModel.ReportViewModel
import kotlinx.android.synthetic.main.fragment_user_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class UserDetailFragment : BaseFragment() {
  private var mUserDetailView: FragmentUserDetailBinding? = null
  private lateinit var mUserListViewModel: ReportViewModel
  private var mSelectedUserId = ""
  private var mUserName = ""

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    mUserListViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
    mUserDetailView = FragmentUserDetailBinding.inflate(layoutInflater, container, false)
    initViews()
    return mUserDetailView!!.root
  }

  private fun initViews() {
    mSelectedUserId = requireArguments().getString(AppConstants.SELECTED_USER_ID)!!

    val userDetails = mUserListViewModel.getUerDataFromDb(mSelectedUserId)
    mUserName = userDetails!!.name!!
    Glide.with(requireActivity())
      .load(userDetails.large)
      .circleCrop()
      .into(mUserDetailView!!.ivUserDetailProfile)

    mUserDetailView!!.ivUserDetailProfile.setOnClickListener {
      //Display the user profile image in full screen
      showUserImageDialog(userDetails)
    }

    //Populate the user detail view items
    loadLayoutViews(mUserListViewModel.userDetailsData(userDetails))

    mUserDetailView!!.llRefreshWeather.setOnClickListener {
      getWeatherForUserLocation(userDetails)
    }
  }

  @SuppressLint("SimpleDateFormat")
  private fun getWeatherForUserLocation(userDetails: UserListModel) {
    if (isNetworkAvailable()) {
      mUserDetailView!!.tvUpdatedDate.text = getString(R.string.fetching)
      mUserListViewModel.fetchWeatherReport(
        userDetails.latitude!!,
        userDetails.longitude!!
      ).observe(viewLifecycleOwner, {
        if (it.status == Resource.Status.SUCCESS) {
          val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm aa")
          val formattedDate: String =
            getString(R.string.updated_at) + dateFormat.format(Date()).toString()
          mUserDetailView!!.tvUpdatedDate.text = formattedDate
          val weatherData = it.data as WeatherDataEntity
          updateWeatherUI(weatherData)
        }
      })
    }
  }

  private fun showUserImageDialog(userDetails: UserListModel) {
    val dialog = Dialog(requireActivity(), android.R.style.Theme_Light)
    dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window!!.setLayout(
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.MATCH_PARENT
    )
    dialog.setContentView(R.layout.dialog_user_image_full_view)
    val userImage = dialog.findViewById<ImageView>(R.id.iv_user_image)
    val imageBack = dialog.findViewById<ImageView>(R.id.iv_back)
    imageBack.setOnClickListener { dialog.dismiss() }

    Glide.with(requireActivity())
      .load(userDetails.large)
      .into(userImage)

    dialog.show()
  }

  private fun loadLayoutViews(detailList: ArrayList<UserDetailsModel>) {
    for (detail in detailList) {
      val llAppointmentContainer: InflateUserReportDetailItemBinding =
        InflateUserReportDetailItemBinding.inflate(
          layoutInflater,
          mUserDetailView!!.llUserDetailContainer,
          false
        )
      llAppointmentContainer.tvDetailTitle.text = detail.title
      llAppointmentContainer.tvDetailDescription.text = detail.description
      mUserDetailView!!.llUserDetailContainer.addView(llAppointmentContainer.root)
    }
  }

  override fun onResume() {
    super.onResume()
    (activity as HomeActivity).showSearchMenu(false, mUserName)
  }

  private fun updateWeatherUI(weatherData: WeatherDataEntity) {
    val tempResult: Double = weatherData.main!!.temp - 273.15
    val roundedValue = round(tempResult).toInt().toString() + AppConstants.KEY_DEGREE
    tv_temperature.text = roundedValue
    tv_city.text = weatherData.name
    tv_weather_state.text = weatherData.weather?.get(0)!!.main
    val resourceID = resources.getIdentifier(
      mUserListViewModel.updateWeatherIcon(weatherData.weather?.get(0)!!.id),
      AppConstants.DRAWABLE,
      requireActivity().packageName
    )
    mUserDetailView!!.ivWeatherIcon.setImageResource(resourceID)
  }

}