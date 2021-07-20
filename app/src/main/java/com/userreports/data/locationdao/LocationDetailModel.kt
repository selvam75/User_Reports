package com.userreports.data.locationdao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "locationDetail")
class LocationDetailModel {

  @Expose
  @SerializedName("id")
  @ColumnInfo(name = "id")
  @PrimaryKey
  var id: String = "1"

  @SerializedName("latitude")
  @Expose
  @ColumnInfo(name = "latitude")
  var latitude: String? = "0"

  @SerializedName("longitude")
  @Expose
  @ColumnInfo(name = "longitude")
  var longitude: String? = "0"

  @SerializedName("updated_at")
  @Expose
  @ColumnInfo(name = "updated_at")
  var updatedAt: String? = ""
}