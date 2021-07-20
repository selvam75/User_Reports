package com.userreports.data.locationdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
  @get:Query("Select * from locationDetail")
  val locationData: LiveData<LocationDetailModel>?

  @Query("DELETE FROM locationDetail")
  fun deleteLocationData()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertLocationData(locationData: LocationDetailModel)
}