package com.smarteist.rcinfinitescroll

import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class InfiniteScroll : RecyclerView.OnScrollListener() {

    /**
     * Setup infinite scrolling behavior for reverse scrolling direction
     */
    private var isReverse: Boolean
    /**
     * Counter for scrolling steps
     */
    var currentPage: Int = 0
    /**
     * Number of items per page
     */
    private var itemsPerPage = 0
    /**
     * [RecyclerView] that we want to provide infinite scrolling behavior for it
     */
    private var recyclerView: RecyclerView? = null
    /**
     * listener that trigger when user reach end of list.
     */
    private var onLoadMoreListener: OnLoadMoreListener? = null
    /**
     * [RecyclerView.LayoutManager] that is attached to [.recyclerView]
     * used to determine [.lastVisibleItem],[.totalItemCount]
     */
    private var layoutManager: RecyclerView.LayoutManager? = null
    /**
     * position of last visible item
     */
    private var lastVisibleItem = 0
    /**
     * position of first visible item
     */
    private var firstVisibleItem = 0
    /**
     * total items badgeCount of [.recyclerView]
     */
    private var totalItemCount = 0
    /**
     * [.onLoadMoreListener] called when [.recyclerView] reach to item with position [.totalItemCount]
     */
    private var threshold: Int
    /**
     * span count of [.layoutManager]
     */
    private var spanCount: Int
    /**
     * determines is nested scroll or not
     */
    private var nestedScroll: Boolean

    init {
        nestedScroll = false
        isReverse = false
        threshold = 3
        spanCount = 1
        currentPage = 0
    }

    /**
     * this function attach [.recyclerView] to provide infinite scroll for it
     *
     * @param recyclerView     see [.recyclerView] for more information
     * @param itemsPerPage     Specifies how many items appear on each page
     * @param reverseScrolling If the scroll is from the bottom to up,
     * like a chat page, infiniteScroll can be mirrored
     */
    fun attach(recyclerView: RecyclerView, itemsPerPage: Int, reverseScrolling: Boolean = false) {
        this.isReverse = reverseScrolling
        this.itemsPerPage = itemsPerPage
        this.recyclerView = recyclerView
        this.layoutManager = recyclerView.layoutManager
        if (this.layoutManager != null) {
            recyclerView.addOnScrollListener(this)
        }
    }

    /**
     * this function detaches [.recyclerView] from this infinite scroll.
     */
    fun detach() {
        try {
            recyclerView?.removeOnScrollListener(this)
            currentPage = 0
            Log.i("InfiniteScroll", "detach: Done")
        } catch (e: NullPointerException) {
            Log.i("InfiniteScroll", "detach: No recycler attached")
        }
    }

    /**
     * @param onLoadMoreListener callback for notifying when user reach list ends.
     */
    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    /**
     * manually calling [OnLoadMoreListener]
     */
    fun retryLoadMore() {
        onLoadMoreListener?.onLoadMore(currentPage)
    }

    /**
     * @param threshold Setting the infinite scroll loading threshold
     * it can't be smaller than 2 for logical reasons!
     */
    fun setThreshold(threshold: Int) {
        if (threshold <= 2) {
            this.threshold = 2
        } else {
            this.threshold = threshold
        }
    }

    fun setHasNestedScroll(nestedScroll: Boolean) {
        this.nestedScroll = nestedScroll
    }

    /**
     * this function get scrolling control of [.recyclerView] and whenever
     * user reached list ends, [.onLoadMoreListener] will be called
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisibleItem = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                firstVisibleItem = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                spanCount = (layoutManager as GridLayoutManager).spanCount
            }
            is LinearLayoutManager -> {
                lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                val firstVisibleItemPositions =
                    (layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
                firstVisibleItem = getFirstVisibleItem(firstVisibleItemPositions)
                spanCount = (layoutManager as StaggeredGridLayoutManager).spanCount
            }
        }
        if (nestedScroll) {
            checkLastItemCorrectness()
        }
        totalItemCount = try {
            layoutManager!!.itemCount
        } catch (e: NullPointerException) {
            0
        }
        if (totalItemCount > threshold && itemsPerPage > 0) {
            if (checkReachedTheThreshold(spanCount * threshold)) {
                val step = (totalItemCount + itemsPerPage - 1) / itemsPerPage
                if (step > currentPage) {
                    currentPage = step
                    onLoadMoreListener?.onLoadMore(currentPage)
                    Log.i("InfiniteScroll", "End Of the List This is step : $currentPage")
                }
            }
        }
        super.onScrolled(recyclerView, dx, dy)
    }

    /**
     * Checking correctness of the {@see lastVisibleItem}
     * Some parent views like nested scroll view affects
     * recycler scrolling behaviour and this prevents
     * the layout manager from returning the last visible item correctly
     * so we're checking correctness
     */
    private fun checkLastItemCorrectness() {
        // Current window height pixels
        val windowHeight: Int = Resources.getSystem().displayMetrics.heightPixels
        val item: View? = layoutManager!!.getChildAt(0)
        if (item != null) {
            val itemHeight: Int = item.height
            val maxVisibleItem =
                spanCount + firstVisibleItem + (windowHeight + itemHeight - 1) / itemHeight * spanCount
            if (lastVisibleItem > maxVisibleItem) {
                lastVisibleItem = maxVisibleItem
            }
        }
    }

    /**
     * @return check if scroll position is on threshold or not
     * it depends on {@param isReverse} which specifies
     * the direction of the infinite scroll
     */
    private fun checkReachedTheThreshold(threshold: Int): Boolean {
        return if (isReverse) {
            firstVisibleItem < threshold
        } else {
            lastVisibleItem > totalItemCount - threshold
        }
    }

    /**
     * @return Last visible item position for staggeredGridLayoutManager
     */
    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (position in lastVisibleItemPositions) {
            if (position > maxSize) {
                maxSize = position
            }
        }
        return maxSize
    }

    /**
     * @return First visible item position for staggeredGridLayoutManager
     */
    private fun getFirstVisibleItem(firstVisibleItemPositions: IntArray): Int {
        var minSize = 0
        if (firstVisibleItemPositions.isNotEmpty()) {
            minSize = firstVisibleItemPositions[0]
            for (position in firstVisibleItemPositions) {
                if (position < minSize) {
                    minSize = position
                }
            }
        }
        return minSize
    }

    interface OnLoadMoreListener {
        /**
         * Created by Ali Hosseini on 8/8/16.
         * callback for notify View when user reached to list ends.
         *
         * @param step indicating steps of the distance between the beginning
         * and current point by counting them from zero to n.
         */
        fun onLoadMore(step: Int)
    }

}