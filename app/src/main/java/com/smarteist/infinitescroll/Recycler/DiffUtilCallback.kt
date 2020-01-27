package com.smarteist.infinitescroll.Recycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult


/**
 * It's more simple callback for calculating the diff between two non-null items in a list.
 *
 *
 * [DiffUtil.Callback()] serves two roles - list indexing, and item diffing. ItemCallback handles
 * just the second of these, which allows separation of code that indexes into an array or List
 * from the presentation-layer and content specific diffing code.
 *
 * @param <T> Type of items to compare.
</T> */
abstract class DiffUtilCallback<T> : DiffUtil.Callback() {
    private var oldItemsList: MutableList<T> = mutableListOf()
    private var newItemsList: MutableList<T> = mutableListOf()

    fun calculateDiff(
        oldItems: MutableList<T>,
        newItems: MutableList<T>,
        detectMoves: Boolean = false
    ): DiffResult {
        oldItemsList = oldItems
        newItemsList = newItems
        return DiffUtil.calculateDiff(this, detectMoves)
    }

    fun calculateDiff(
        newItems: MutableList<T>, detectMoves: Boolean = false
    ): DiffResult {
        return this.calculateDiff(this.newItemsList, newItems, detectMoves)
    }

    override fun getOldListSize(): Int {
        return oldItemsList.size
    }

    override fun getNewListSize(): Int {
        return newItemsList.size
    }

    fun getNewList(): MutableList<T> {
        return newItemsList
    }

    fun getOldList(): MutableList<T> {
        return oldItemsList
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemsList[oldItemPosition], newItemsList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItemsList[oldItemPosition], newItemsList[newItemPosition])
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}