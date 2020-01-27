package com.smarteist.infinitescroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smarteist.infinitescroll.Model.Generator
import com.smarteist.infinitescroll.Recycler.RecyclerAdapter
import com.smarteist.rcinfinitescroll.InfiniteScroll

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView
    lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        val recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        val infiniteScroll = InfiniteScroll()

        recyclerAdapter.addList(Generator.getPosts(8))
        infiniteScroll.retryLoadMore()

        infiniteScroll.attach(recyclerView, 8)
        infiniteScroll.setOnLoadMoreListener(object : InfiniteScroll.OnLoadMoreListener {
            override fun onLoadMore(step: Int) {
                progressBar.visibility = View.VISIBLE
                Handler().postDelayed({

                    recyclerAdapter.addList(Generator.getPosts(8))
                    progressBar.visibility = View.GONE


                }, 2000)
            }

        })


    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rv_mainActivity)
        progressBar = findViewById(R.id.pb_mainActivity)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }


}
