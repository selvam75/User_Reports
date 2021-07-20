package com.userreports.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherDataEntity {

  @Expose
  @SerializedName("cod")
   var cod = 0

  @Expose
  @SerializedName("name")
   var name: String? = null

  @Expose
  @SerializedName("id")
   var id = 0

  @Expose
  @SerializedName("timezone")
   var timezone = 0

  @Expose
  @SerializedName("sys")
   var sys: SysEntity? = null

  @Expose
  @SerializedName("dt")
   var dt = 0

  @Expose
  @SerializedName("clouds")
   var clouds: CloudsEntity? = null

  @Expose
  @SerializedName("wind")
   var wind: WindEntity? = null

  @Expose
  @SerializedName("visibility")
   var visibility = 0

  @Expose
  @SerializedName("main")
   var main: MainEntity? = null

  @Expose
  @SerializedName("base")
   var base: String? = null

  @Expose
  @SerializedName("weather")
   var weather: List<WeatherEntity?>? = null

  @Expose
  @SerializedName("coord")
   var coord: CoordEntity? = null

  class SysEntity {
    @Expose
    @SerializedName("sunset")
    var sunset = 0

    @Expose
    @SerializedName("sunrise")
    var sunrise = 0

    @Expose
    @SerializedName("country")
    var country: String? = null

    @Expose
    @SerializedName("id")
    var id = 0

    @Expose
    @SerializedName("type")
    var type = 0
  }

  class CloudsEntity {
    @Expose
    @SerializedName("all")
    var all = 0
  }

  class WindEntity {
    @Expose
    @SerializedName("gust")
    var gust = 0.0

    @Expose
    @SerializedName("deg")
    var deg = 0

    @Expose
    @SerializedName("speed")
    var speed = 0.0
  }

  class MainEntity {
    @Expose
    @SerializedName("humidity")
    var humidity = 0

    @Expose
    @SerializedName("pressure")
    var pressure = 0

    @Expose
    @SerializedName("temp_max")
    var tempMax = 0.0

    @Expose
    @SerializedName("temp_min")
    var tempMin = 0.0

    @Expose
    @SerializedName("feels_like")
    var feelsLike = 0.0

    @Expose
    @SerializedName("temp")
    var temp = 0.0
  }

  class WeatherEntity {
    @Expose
    @SerializedName("icon")
    var icon: String? = null

    @Expose
    @SerializedName("description")
    var description: String? = null

    @Expose
    @SerializedName("main")
    var main: String? = null

    @Expose
    @SerializedName("id")
    var id = 0
  }

  class CoordEntity {
    @Expose
    @SerializedName("lat")
    var lat = 0.0

    @Expose
    @SerializedName("lon")
    var lon = 0.0
  }
}