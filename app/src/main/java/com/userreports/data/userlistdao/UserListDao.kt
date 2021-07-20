package com.userreports.data.userlistdao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserListDao {
  @get:Query("Select * from userListData")
  val allUsersList: LiveData<MutableList<UserListModel>>

  @Query("SELECT * FROM userListData WHERE id = :uuid")
  fun findByUserId(uuid: String?): UserListModel?

  @Query("DELETE FROM userListData")
  fun deleteUsersList()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertUsersList(message: MutableList<UserListModel>)
}