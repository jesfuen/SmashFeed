package com.example.smashfeed.ui.profile.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.smashfeed.R
import com.example.smashfeed.data.model.PostWithUser
import java.io.File

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private var currentList: List<PostWithUser> = emptyList()

    fun submitList(newList: List<PostWithUser>) {
        currentList = newList
        notifyDataSetChanged()
    }

    class ProfileViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val ivPostImage: ImageView = row.findViewById(R.id.ivPostImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_grid, parent, false)
        return ProfileViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val file = File(currentList[position].post.path)
        if (file.exists()) {
            holder.ivPostImage.setImageURI(Uri.fromFile(file))
        }
    }

    override fun getItemCount(): Int = currentList.size
}