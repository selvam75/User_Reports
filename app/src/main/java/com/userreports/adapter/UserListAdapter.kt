package com.userreports.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.userreports.R
import com.userreports.base.AdapterCallback
import com.userreports.data.userlistdao.UserListModel
import java.util.*

import kotlin.collections.ArrayList


class UserListAdapter() : RecyclerView.Adapter<UserListAdapter.CommonViewHolder>(), Filterable {

  var mTempList: ArrayList<UserListModel> = ArrayList()
  private var mCallback: AdapterCallback? = null
  private var mUserList: ArrayList<UserListModel>? = null

  constructor(userList: ArrayList<UserListModel>?, callback: AdapterCallback) : this() {
    mTempList.addAll(userList!!)
    mUserList = userList
    mCallback = callback
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.inflate_user_item_view, parent, false)
    return CommonViewHolder(view)
  }

  override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
    val userData = mUserList!![position]
    holder.setData(userData)
    holder.itemView.setOnClickListener {
      mCallback!!.onItemClicked(position)
    }
  }

  override fun getItemCount(): Int {
    return mUserList!!.size
  }

  override fun getFilter(): Filter {
    return userFilter
  }

  fun updateUserList(newUserList: ArrayList<UserListModel>) {
    mUserList!!.addAll(newUserList)
    notifyDataSetChanged()
  }

  inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tv_user_name)
    private val ivGraphicalView: ImageView = itemView.findViewById(R.id.iv_user_profile)
    fun setData(userListItem: UserListModel) {
      tvTitle.text = userListItem.name
      Glide.with(ivGraphicalView.context)
        .load(userListItem.large)
        .circleCrop()
        .into(ivGraphicalView)
    }
  }

  private val userFilter: Filter = object : Filter() {
    override fun performFiltering(constraint: CharSequence): FilterResults {
      val filteredList: MutableList<UserListModel> = ArrayList()
      if (constraint.isEmpty()) {
        filteredList.addAll(mTempList)
      } else {
        val filterPattern =
          constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
        for (item in mTempList) {
          if (item.name!!.lowercase(Locale.getDefault()).contains(filterPattern)) {
            filteredList.add(item)
          }
        }
      }
      val results = FilterResults()
      results.values = filteredList
      return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
      mUserList!!.clear()
      mUserList!!.addAll(results.values as ArrayList<UserListModel>)
      notifyDataSetChanged()
    }
  }
}
