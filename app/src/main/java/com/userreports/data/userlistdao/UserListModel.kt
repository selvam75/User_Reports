package com.userreports.data.userlistdao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "userListData")
class UserListModel {

  @Expose
  @SerializedName("id")
  @ColumnInfo(name = "id")
  @PrimaryKey
  var id: String = ""

  @SerializedName("email")
  @Expose
  @ColumnInfo(name = "email")
  var email: String? = "--"

  @SerializedName("phone")
  @Expose
  @ColumnInfo(name = "phone")
  var phone: String? = "--"

  @SerializedName("cell")
  @Expose
  @ColumnInfo(name = "cell")
  var cell: String? = "--"

  @SerializedName("name")
  @Expose
  @ColumnInfo(name = "name")
  var name: String? = "--"

  @SerializedName("street")
  @Expose
  @ColumnInfo(name = "street")
  var street: String? = "--"

  @SerializedName("age")
  @Expose
  @ColumnInfo(name = "age")
  var age: Int? = 0

  @SerializedName("thumbnail")
  @Expose
  @ColumnInfo(name = "thumbnail")
  var thumbnail: String? = "--"

  @SerializedName("large")
  @Expose
  @ColumnInfo(name = "large")
  var large: String? = "--"

  @SerializedName("gender")
  @Expose
  @ColumnInfo(name = "gender")
  var gender: String? = "--"

  @SerializedName("latitude")
  @Expose
  @ColumnInfo(name = "latitude")
  var latitude: String? = "0"

  @SerializedName("longitude")
  @Expose
  @ColumnInfo(name = "longitude")
  var longitude: String? = "0"

}