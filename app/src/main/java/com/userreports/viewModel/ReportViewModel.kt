package com.userreports.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import com.userreports.UserReportApplication
import com.userreports.base.AppConstants
import com.userreports.base.EndPoints
import com.userreports.base.Resource
import com.userreports.data.AppDatabase
import com.userreports.data.UserDetailsModel
import com.userreports.data.UserListItemEntity
import com.userreports.data.WeatherDataEntity
import com.userreports.data.locationdao.LocationDetailModel
import com.userreports.data.userlistdao.UserListModel
import org.json.JSONObject

class ReportViewModel : ViewModel() {

  fun getUerDataFromDb(mSelectedUserId: String): UserListModel? {
    return AppDatabase.getInstance(UserReportApplication.instances!!)!!.userListDao()!!
      .findByUserId(mSelectedUserId)
  }

  fun userDetailsData(userDetails: UserListModel): java.util.ArrayList<UserDetailsModel> {
    val detailList = java.util.ArrayList<UserDetailsModel>()
    detailList.add(UserDetailsModel(AppConstants.KEY_NAME, userDetails.name!!.toString()))

    if (userDetails.email != null)
      detailList.add(UserDetailsModel(AppConstants.KEY_EMAIL, userDetails.email!!.toString()))

    detailList.add(UserDetailsModel(AppConstants.KEY_PHONE, userDetails.phone!!.toString()))
    detailList.add(UserDetailsModel(AppConstants.KEY_CELL, userDetails.cell!!.toString()))
    detailList.add(UserDetailsModel(AppConstants.KEY_AGE, userDetails.age!!.toString()))
    detailList.add(UserDetailsModel(AppConstants.KEY_GENDER, userDetails.gender!!.toString()))
    detailList.add(UserDetailsModel(AppConstants.KEY_ADDRESS, userDetails.street!!.toString()))
    return detailList
  }

  var mCanPaginate = true
  fun getUserLists(pageLimit: Int) = MutableLiveData<Resource<MutableList<UserListModel>>>().apply {
    AndroidNetworking.get(EndPoints.API_END_URL)
      .addQueryParameter(EndPoints.PARAMS_RESULT, pageLimit.toString())
      .setPriority(Priority.MEDIUM)
      .build()

      .getAsJSONObject(object : JSONObjectRequestListener {
        override fun onResponse(response: JSONObject) {
          val userResponse = Gson().fromJson(response.toString(), UserListItemEntity::class.java)
          val userDataList = ArrayList<UserListModel>()
          mCanPaginate = userResponse.getInfo()!!.results!! > 1
          for (item in userResponse.getResults()!!) {
            val userdata = UserListModel()
            userdata.age = item!!.dob!!.age
            userdata.name = item.name!!.title + ". " + item.name!!.first + " " + item.name!!.last
            userdata.gender = item.gender
            userdata.cell = item.cell
            userdata.phone = item.phone
            userdata.id = item.login!!.uuid!!
            userdata.large = item.picture!!.large!!
            userdata.thumbnail = item.picture!!.thumbnail!!
            userdata.latitude = item.location!!.coordinates!!.latitude!!
            userdata.longitude = item.location!!.coordinates!!.longitude!!
            userdata.street =
              item.location!!.street!!.number!!.toString() + ", " + item.location!!.street!!.name!! + ", " + item.location!!.city!! + ", " + item.location!!.state!! + ", " + item.location!!.country!! + ", " + item.location!!.postcode!!
            userDataList.add(userdata)
            AppDatabase.getInstance(UserReportApplication.instances!!)!!.userListDao()!!
              .insertUsersList(userDataList)
          }
          value = Resource.success(userDataList)
        }

        override fun onError(error: ANError) {
          value = if (error.errorCode != 0) {
            Resource.error(error.errorDetail, null)
          } else {
            Resource.error(error.errorDetail, null)
          }
        }
      })
  }

  fun fetchWeatherReport(latitude: String, longitude: String) =
    MutableLiveData<Resource<WeatherDataEntity>>().apply {
      AndroidNetworking.get(EndPoints.WEATHER_API_END_URL)
        .addQueryParameter(EndPoints.PARAMS_APP_ID, EndPoints.WEATHER_APP_ID)
        .addQueryParameter(EndPoints.PARAMS_LATITUDE, latitude)
        .addQueryParameter(EndPoints.PARAMS_LONGITUDE, longitude)
        .setPriority(Priority.MEDIUM)
        .build()

        .getAsJSONObject(object : JSONObjectRequestListener {
          override fun onResponse(response: JSONObject) {
            value =
              Resource.success(Gson().fromJson(response.toString(), WeatherDataEntity::class.java))
          }

          override fun onError(error: ANError) {
            value = if (error.errorCode != 0) {
              Resource.error(error.errorDetail, null)
            } else {
              Resource.error(error.errorDetail, null)
            }
          }
        })
    }

  fun updateWeatherIcon(condition: Int): String {
    when (condition) {
      in 0..300 -> {
        return "thunderstorm"
      }
      in 300..500 -> {
        return "lightrain"
      }
      in 500..600 -> {
        return "shower"
      }
      in 600..700 -> {
        return "snow_dark"
      }
      in 701..771 -> {
        return "fog"
      }
      in 772..800 -> {
        return "overcast"
      }
      800 -> {
        return "sunny"
      }
      in 801..804 -> {
        return "cloudy"
      }
      in 900..902 -> {
        return "thunderstorm"
      }
      903 -> {
        return "snow"
      }
      904 -> {
        return "sunny"
      }
      in 905..1000 -> {
        return "thunderstorm_heavy"
      }
      else -> return "Cant Fetch The Details For Now!"
    }
  }

  fun saveLocationData(latitude: String, longitude: String, formattedDate: String) {
    AppDatabase.getInstance(UserReportApplication.instances!!)!!.locationDao()!!
      .deleteLocationData()

    val locationData = LocationDetailModel()
    locationData.latitude = latitude
    locationData.longitude = longitude
    locationData.updatedAt = formattedDate

    AppDatabase.getInstance(UserReportApplication.instances!!)!!.locationDao()!!
      .insertLocationData(locationData)
  }
}