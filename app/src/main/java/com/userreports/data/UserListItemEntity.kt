package com.userreports.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserListItemEntity {
  @SerializedName("results")
  @Expose
  private var results: List<Result?>? = null

  @SerializedName("info")
  @Expose
  private var info: Info? = null

  fun getResults(): List<Result?>? {
    return results
  }

  fun setResults(results: List<Result?>?) {
    this.results = results
  }

  fun getInfo(): Info? {
    return info
  }

  fun setInfo(info: Info?) {
    this.info = info
  }

  class Info {
    @SerializedName("seed")
    @Expose
    var seed: String? = null

    @SerializedName("results")
    @Expose
    var results: Int? = null

    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null
  }

  class Result {
    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("name")
    @Expose
    var name: Name? = null

    @SerializedName("location")
    @Expose
    var location: Location? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("login")
    @Expose
    var login: Login? = null

    @SerializedName("dob")
    @Expose
    var dob: Dob? = null

    @SerializedName("registered")
    @Expose
    var registered: Registered? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("cell")
    @Expose
    var cell: String? = null

    @SerializedName("id")
    @Expose
    var id: Id? = null

    @SerializedName("picture")
    @Expose
    var picture: Picture? = null

    @SerializedName("nat")
    @Expose
    var nat: String? = null
  }

  class Street {
    @SerializedName("number")
    @Expose
    var number: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
  }

  class Timezone {
    @SerializedName("offset")
    @Expose
    var offset: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null
  }

  class Registered {
    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("age")
    @Expose
    var age: Int? = null
  }

  class Picture {
    @SerializedName("large")
    @Expose
    var large: String? = null

    @SerializedName("medium")
    @Expose
    var medium: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null
  }

  class Name {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("first")
    @Expose
    var first: String? = null

    @SerializedName("last")
    @Expose
    var last: String? = null
  }

  class Login {
    @SerializedName("uuid")
    @Expose
    var uuid: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("salt")
    @Expose
    var salt: String? = null

    @SerializedName("md5")
    @Expose
    var md5: String? = null

    @SerializedName("sha1")
    @Expose
    var sha1: String? = null

    @SerializedName("sha256")
    @Expose
    var sha256: String? = null
  }

  class Location {
    @SerializedName("street")
    @Expose
    var street: Street? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("postcode")
    @Expose
    var postcode: String? = null

    @SerializedName("coordinates")
    @Expose
    var coordinates: Coordinates? = null

    @SerializedName("timezone")
    @Expose
    var timezone: Timezone? = null
  }

  class Dob {
    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("age")
    @Expose
    var age: Int? = null
  }

  class Coordinates {
    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null
  }

  class Id {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("value")
    @Expose
    var value: String? = null
  }
}