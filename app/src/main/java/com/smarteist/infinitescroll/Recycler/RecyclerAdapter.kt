package com.smarteist.infinitescroll.Recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.infinitescroll.Model.Post
import com.smarteist.infinitescroll.R

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.recyclerVH>() {

    private val diffUtilCallback: DiffUtilCallback<Post> = object : DiffUtilCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.equals(newItem)
        }
    }

    class recyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewPost: ImageView = itemView.findViewById(R.id.iv_rc_main_item)
        var textViewTitle: TextView = itemView.findViewById(R.id.tv_title_rc_main_item)
        var textViewContent: TextView = itemView.findViewById(R.id.tv_description_rc_main_item)
    }


    fun addList(items: MutableList<Post>) {
        val new = diffUtilCallback.getNewList()
        new.addAll(items)
        diffUtilCallback
            .calculateDiff(diffUtilCallback.getNewList(), new, true)
            .dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recyclerVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, null)
        return recyclerVH(inflate)
    }

    override fun getItemCount(): Int {
        return diffUtilCallback.newListSize
    }

    override fun onBindViewHolder(holder: recyclerVH, position: Int) {
        val post = diffUtilCallback.getNewList()[position]
        holder.imageViewPost.setImageResource(post.imageId)
        holder.textViewTitle.text = post.title
        holder.textViewContent.text = post.content
    }
}