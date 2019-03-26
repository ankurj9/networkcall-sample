package com.rygelouv.networkcalldslsample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scrolling.*
import com.google.android.material.appbar.AppBarLayout
import com.rygelouv.networkcalldslsample.adapter.AdListAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: AdListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = getString(R.string.app_name)
                    fab.show()
                    adButton.visibility = View.GONE
                    isShow = true
                } else if (isShow) {
                    fab.hide()
                    adButton.visibility = View.VISIBLE
                    collapsingToolbarLayout.setTitle(" ")//carefull there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })
        adapter = AdListAdapter(ArrayList())
        repoList.layoutManager = LinearLayoutManager(this)
        repoList.adapter = adapter

        Repository.getRepos("kotlin").observe(this, Observer {
            when (it?.status) {
                Resource.LOADING -> {
                    Log.d("MainActivity", "--> Loading...")
                    loadingWrapper.visibility = View.VISIBLE
                    listWrapper.visibility = View.GONE
                }
                Resource.SUCCESS -> {
                    Log.d("MainActivity", "--> Success! | loaded ${it.data?.size ?: 0} records.")

                    loadingWrapper.visibility = View.GONE
                    listWrapper.visibility = View.VISIBLE
                    adapter.replace(it.data ?: ArrayList())
                }
                Resource.ERROR -> {
                    Log.d("MainActivity", "--> Error!")
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
