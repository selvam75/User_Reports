package com.userreports.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import com.userreports.R
import com.userreports.base.BaseActivity
import com.userreports.fragments.UserListFragment

class HomeActivity : BaseActivity() {
  private var searchItem: MenuItem? = null
  private var searchView: SearchView? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mActivity = this
    setContentView(R.layout.activity_home)
    loadFragment(UserListFragment())
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.search_menu, menu)

    searchItem = menu.findItem(R.id.actionSearch)

    searchView = searchItem!!.actionView as SearchView
    searchView!!.imeOptions = EditorInfo.IME_ACTION_DONE
    searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frm_content_frame)

        if (navHostFragment is UserListFragment) {
          navHostFragment.filterUser(newText)
        }
        return false
      }

    })
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  fun showSearchMenu(show: Boolean, title: String) {
    if (searchItem != null) {
      if (!searchView!!.isIconified) {
        searchView!!.isIconified = true
        searchView!!.onActionViewCollapsed()
      }
      searchItem!!.isVisible = show
    }
    supportActionBar!!.title = title
    supportActionBar!!.setDisplayHomeAsUpEnabled(!show)
    supportActionBar!!.setHomeButtonEnabled(!show)
  }
}