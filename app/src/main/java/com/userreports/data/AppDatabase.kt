package com.userreports.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.userreports.data.locationdao.LocationDao
import com.userreports.data.locationdao.LocationDetailModel
import com.userreports.data.userlistdao.UserListDao
import com.userreports.data.userlistdao.UserListModel

@Database(entities = [UserListModel::class, LocationDetailModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userListDao(): UserListDao?
  abstract fun locationDao(): LocationDao?

  companion object {
    private var appDatabase: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase? {
      if (appDatabase == null) {
        appDatabase = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "users-database"
        ).fallbackToDestructiveMigration()
          .allowMainThreadQueries()
          .build()
      }
      return appDatabase
    }
  }
}